package cron;

public abstract class FixedDelayTriggerTask implements TriggerTask{
	
	private long executeTime;
	private long delay;
	
	public FixedDelayTriggerTask(long initDelay, long delay) {
		this.executeTime = System.currentTimeMillis() + initDelay;
		this.delay = delay;
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
