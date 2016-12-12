package cn.magicstudio.mblog.monitor;

import cn.wonhigh.retail.backend.core.ApplicationContext;
import java.util.Date;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class PerformanceLogger {
	static final XLogger logger = XLoggerFactory
			.getXLogger(PerformanceLogger.class);
	static final XLogger log = XLoggerFactory
			.getXLogger(PerformanceManager.class);

	static PerformancePublisher publisher = null;

	private static PerformancePublisher getPerformancePublisher() {
		return publisher;
	}

	private static void notify(String app, String type, String uri,
			String status, long takes, String tag) {
		if (getPerformancePublisher() == null) {
			return;
		}
		Counter c = new Counter();
		c.setDate(new Date());
		c.setStatus(status);
		c.setTag(tag);
		c.setTakes(takes);
		c.setUri(uri);
		c.setType(type);
		c.setApp(app);
		try {
			getPerformancePublisher().notify(c);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}
	}

	public static void log(String app, String type, String uri, String status,
			long takes, String tag) {
		String msg = " ==> " + type + ":" + uri + " takes:" + takes + " ms ,"
				+ status + "," + tag;

		if (takes > 5000L) {
			logger.error(msg);
		} else if (takes > 200L) {
			logger.warn(msg);
		} else {
			logger.info(msg);
		}
		notify(app, type, uri, status, takes, tag);
	}

	public static void log(String type, String uri, String status, long takes,
			String tag) {
		log(ApplicationContext.getAppCode(), type, uri, status, takes, tag);
	}

	public static void log(String app, String type, String uri, String status,
			long takes) {
		log(app, type, uri, status, takes, "");
	}

	public static void log(String type, String uri, String status, long takes) {
		log(ApplicationContext.getAppCode(), type, uri, status, takes, "");
	}
}
