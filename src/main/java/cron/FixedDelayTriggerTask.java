package cron;

import java.util.concurrent.TimeUnit;

public abstract class FixedDelayTriggerTask implements TriggerTask{
	
	private long executeTime;
	private long delay;
	
	public FixedDelayTriggerTask(long initDelay, long delay, TimeUnit timeUnit) {
		this.executeTime = System.currentTimeMillis() + timeUnit.toMillis(initDelay);
		this.delay = timeUnit.toMillis(delay);
	}
	
	protected abstract void handle(long time);

	public void trigger(long time) {
		handle(time);
		this.executeTime = System.currentTimeMillis() + delay;
	}

	public long getTriggerTime() {
		return executeTime;
	}

	public boolean canTrigger() {
		return true;
	}

}
