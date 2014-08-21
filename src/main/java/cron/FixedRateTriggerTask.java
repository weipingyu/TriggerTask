package cron;

import java.util.concurrent.TimeUnit;

public abstract class FixedRateTriggerTask implements TriggerTask{
	
	private long executeTime;
	private long delay;
	
	public FixedRateTriggerTask(long initDelay, long delay, TimeUnit timeUnit) {
		this.executeTime = System.currentTimeMillis() + timeUnit.toMillis(initDelay);
		this.delay = timeUnit.toMillis(delay);
	}
	
	protected abstract void handle(long time);

	public void trigger(long time) {
		handle(time);
		this.executeTime = this.executeTime + delay;
	}

	public long getTriggerTime() {
		return executeTime;
	}

	public boolean canTrigger() {
		return true;
	}

}
