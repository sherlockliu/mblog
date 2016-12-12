package cn.magicstudio.mblog.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class Log4jUtil implements InitializingBean {
	private static final XLogger log = XLoggerFactory
			.getXLogger(Log4jUtil.class);

	private void inti() {
		String env = System.getProperty("env");
		if ("online".equals(env)) {
			removeConsoleAppender();
			setBufferIoTrue();
			addShutdownHookFlushLog();
			log.info("★★★★★★★★★★★★★★★★正式环境日志初始化完成★★★★★★★★★★★★★★★★");
		} else if ("train".equals(env)) {
			removeConsoleAppender();
			setBufferIoTrue();
			addShutdownHookFlushLog();
			log.info("★★★★★★★★★★★★★★★★培训环境日志初始化完成★★★★★★★★★★★★★★★★");
		} else {
			log.info("★★★★★★★★★★★★★★★★开发环境日志初始化完成★★★★★★★★★★★★★★★★");
		}
	}

	public static void flushImmediately() {
		Logger rootLogger = Logger.getRootLogger();
		flushImmediately(rootLogger);

		Enumeration currentLoggers = LogManager.getCurrentLoggers();
		while (currentLoggers.hasMoreElements()) {
			Logger logger = (Logger) currentLoggers.nextElement();
			flushImmediately(logger);
		}
	}

	public static void addShutdownHookFlushLog() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
			}
		}));
	}

	public static void setBufferIoTrue() {
		Logger rootLogger = Logger.getRootLogger();
		setBufferIoTrue(rootLogger);

		Enumeration currentLoggers = LogManager.getCurrentLoggers();
		while (currentLoggers.hasMoreElements()) {
			Logger logger = (Logger) currentLoggers.nextElement();
			setBufferIoTrue(logger);
		}
	}

	private static void flushImmediately(Logger logger) {
		Enumeration allAppenders = logger.getAllAppenders();
		while (allAppenders.hasMoreElements()) {
			Appender appender = (Appender) allAppenders.nextElement();
			if ((appender instanceof FileAppender)) {
				FileAppender rollingFileAppender = (FileAppender) appender;
				if (!rollingFileAppender.getImmediateFlush()) {
					rollingFileAppender.setImmediateFlush(true);
					logger.info("flush Immediate ");
					rollingFileAppender.setImmediateFlush(false);
				}
			}
		}
	}

	private static void setBufferIoTrue(Logger logger) {
		Enumeration allAppenders = logger.getAllAppenders();
		while (allAppenders.hasMoreElements()) {
			Appender appender = (Appender) allAppenders.nextElement();
			if ((appender instanceof FileAppender)) {
				FileAppender rollingFileAppender = (FileAppender) appender;
				rollingFileAppender.setBufferedIO(true);
			}
		}
	}

	public static void removeConsoleAppender() {
		Logger rootLogger = Logger.getRootLogger();
		removeConsoleAppender(rootLogger);
		Enumeration currentLoggers = LogManager.getCurrentLoggers();
		while (currentLoggers.hasMoreElements()) {
			Logger logger = (Logger) currentLoggers.nextElement();
			removeConsoleAppender(logger);
		}
	}

	private static void removeConsoleAppender(Logger logger) {
		Enumeration allAppenders = logger.getAllAppenders();
		List<Appender> consoleAppender = new ArrayList();
		while (allAppenders.hasMoreElements()) {
			Appender appender = (Appender) allAppenders.nextElement();
			if ((appender instanceof ConsoleAppender)) {
				consoleAppender.add(appender);
			}
		}

		for (Appender appender : consoleAppender) {
			System.out.println("remove consoleAppender");
			logger.removeAppender(appender);
		}
	}

	public void afterPropertiesSet() throws Exception {
		inti();
	}
}
