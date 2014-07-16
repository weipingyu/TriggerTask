package cron;

public abstract class FixedRateTriggerTask implements TriggerTask{
	
	private long executeTime;
	private long delay;
	
	public FixedRateTriggerTask(long initDelay, long delay) {
		this.executeTime = System.currentTimeMillis() + initDelay;
		this.delay = delay;
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
