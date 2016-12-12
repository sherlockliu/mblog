package cn.magicstudio.mblog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopWatch {
	private Logger logger = null;

	private static final Logger LOGGER = LoggerFactory
			.getLogger("cn.wonhigh.retail.backend.stopWatchLogger");

	private long start;

	public StopWatch(Logger logger) {
		this.logger = logger;
		this.start = System.nanoTime();
	}

	public StopWatch() {
		this.logger = LOGGER;
		this.start = System.nanoTime();
	}

	public void printExecuteTime(String name) {
		double ms = (System.nanoTime() - this.start) / 1000000.0D;

		this.start = System.nanoTime();
	}

	public void printExecuteTime() {
		printExecuteTime("STOP_WATCH");
	}
}