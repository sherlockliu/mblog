package cn.magicstudio.mblog.base.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextHolder implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;
	private static Logger logger = LoggerFactory
			.getLogger(SpringApplicationContextHolder.class);

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		logger.debug("注入ApplicationContext到SpringContextHolder:{}",
				applicationContext);
		if (applicationContext != null) {
			logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
					+ applicationContext);
		}

		applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return (T) applicationContext.getBean(requiredType);
	}
}
