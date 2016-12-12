package cn.magicstudio.mblog.base.framework.utils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class CallerRunsPolicy implements RejectedExecutionHandler {
	private static Logger logger = Logger.getLogger(CallerRunsPolicy.class
			.getName());

	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		if (!e.isShutdown()) {
			r.run();
			logger.warning(" =========== Warning Policy ==== Pls Ajust ThreadPool Size ==== ");
		}
	}
}
