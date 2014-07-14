package cron;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleThreadFactory implements ThreadFactory {
	private AtomicInteger id = new AtomicInteger();
	private String namePrefix;
	private boolean daemon;
	
	public SimpleThreadFactory(String namePrefix) {
		this(namePrefix, false);
	}
	
	public SimpleThreadFactory(String namePrefix, boolean daemon) {
		super();
		this.namePrefix = namePrefix;
		this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, namePrefix + "-" + id.incrementAndGet());
		thread.setDaemon(daemon);
		return thread;
	}

}
