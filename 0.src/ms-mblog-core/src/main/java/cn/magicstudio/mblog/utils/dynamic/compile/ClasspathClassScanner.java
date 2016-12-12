package cn.magicstudio.mblog.utils.dynamic.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathClassScanner implements ClassScanner {
	private static final Logger logger = LoggerFactory
			.getLogger(ClasspathClassScanner.class);

	public Class<?> scan(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error("ERROR ## can not found this class ,the name = "
					+ className);
		}

		return null;
	}
}