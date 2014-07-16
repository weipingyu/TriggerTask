package cron;

public abstract class ImmediateTriggerTask implements TriggerTask {
	
	protected abstract void handle(long time);

	public void trigger(long time) {
		handle(time);
	}

	public long getTriggerTime() {
		return System.currentTimeMillis();
	}

	public boolean canTrigger() {
		return false;
	}

}
