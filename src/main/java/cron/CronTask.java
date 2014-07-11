package cron;


public interface CronTask {
	
	void handle(long runTime);

	String getCron();
}
