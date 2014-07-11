package cron;

public abstract class FixDelayTriggerTask implements TriggerTask{

	public void trigger(long time) {
		// TODO Auto-generated method stub
		
	}

	public long getTriggerTime() {
		return 0;
	}

	public boolean canTrigger() {
		return false;
	}

}
