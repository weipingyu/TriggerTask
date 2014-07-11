package cron;

public interface TriggerTask {

	void trigger(long time);
	
	long getTriggerTime();
	
	boolean canTrigger();
}
