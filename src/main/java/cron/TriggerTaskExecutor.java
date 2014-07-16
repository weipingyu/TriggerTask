package cron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerTaskExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerTaskExecutor.class);
	private ExecutorService service = Executors.newCachedThreadPool(new SimpleThreadFactory("TriggerTaskExecutor"));
	private RefreshDelayQueue<TaskTrigger> taskDelayQueue = new RefreshDelayQueue<TaskTrigger>();
	private static final int thread_num = 1;
	
	public TriggerTaskExecutor() {
		for(int i=0; i < thread_num; i++) {
			service.execute(new Runnable() {
				
				public void run() {
					while(!service.isShutdown()) {
						try {
							TaskTrigger trigger = taskDelayQueue.take();
							trigger.trigger();
							if(trigger.canTrigger()) {
								taskDelayQueue.add(trigger);
							}
						} catch (Exception e) {
							LOGGER.error("", e);
						}
						
					}
					
				}
			});
		}
	}

	public void addTask(TriggerTask task) {
		if(task.canTrigger()) {
			taskDelayQueue.add(new TaskTrigger(task));
		}
	}
	
	public void addAllTask(Collection<? extends TriggerTask> taskCollection) {
		List<TaskTrigger> taskList = new ArrayList<TaskTrigger>(taskCollection.size());
		for(TriggerTask task : taskCollection) {
			if(task.canTrigger()) {
				taskList.add(new TaskTrigger(task));
			}
		}
		taskDelayQueue.addAll(taskList);
	}
	
	public void refresh() {
		taskDelayQueue.refresh();
	}
	
	public void shutdown() {
		service.shutdownNow();
	}
	
	
	private static class TaskTrigger implements Delayed{
		private TriggerTask triggerTask;
		
		public TaskTrigger(TriggerTask triggerTask) {
			this.triggerTask = triggerTask;
		}

		public int compareTo(Delayed o) {
			return (int)(getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
		}

		public long getDelay(TimeUnit unit) {
			long delay = Math.max(getTriggerTime() - System.currentTimeMillis(), 0);
			return unit.convert(delay, TimeUnit.MILLISECONDS);
		}

		public void trigger() {
			try {
				triggerTask.trigger(triggerTask.getTriggerTime());
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		public long getTriggerTime() {
			return triggerTask.getTriggerTime();
		}
		
		public boolean canTrigger() {
			return triggerTask.canTrigger();
		}

	}

	public boolean isShutdown() {
		return service.isShutdown();
	}


	public void execute(final Runnable command) {
		TriggerTask task = new ImmediateTriggerTask() {
			
			@Override
			protected void handle(long time) {
				command.run();
			}
		};
		addTask(task);
	}

	public void schedule(Runnable command, long delay,
			TimeUnit unit) {
		scheduleWithFixedDelay(command, 0, delay, unit);
	}

	public void scheduleAtFixedRate(final Runnable command,
			long initialDelay, long period, TimeUnit unit) {
		TriggerTask task = new FixedRateTriggerTask(unit.toMillis(initialDelay), unit.toMillis(period)) {
			
			@Override
			protected void handle(long time) {
				command.run();
			}
		};
		addTask(task);
	}

	public void scheduleWithFixedDelay(final Runnable command,
			long initialDelay, long delay, TimeUnit unit) {
		TriggerTask task = new FixedDelayTriggerTask(unit.toMillis(initialDelay), unit.toMillis(delay)) {
			
			@Override
			protected void handle(long time) {
				command.run();
			}
		};
		
		addTask(task);
	}
}
