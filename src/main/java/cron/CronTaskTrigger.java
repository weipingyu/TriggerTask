package cron;

public interface CronTaskTrigger extends CronTask {
	
	void trigger();

	long getTriggerTime();
}
