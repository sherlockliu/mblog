package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class FileSystemClassScanner implements InitializingBean, ClassScanner {
	private static final Logger logger = LoggerFactory
			.getLogger(FileSystemClassScanner.class);
	private static final String CLASS_FILE = ".class";
	private static final String JAR_FILE = ".jar";
	private String extendsDir;
	private FileSystemClassLoader fileClassLoader;

	public void afterPropertiesSet() throws Exception {
		this.fileClassLoader = new FileSystemClassLoader(this.extendsDir,
				getClass().getClassLoader());
	}

	public Class<?> scan(String className) {
		return findInDirectory(this.extendsDir, className);
	}

	private Class<?> findInDirectory(String dirStr, String className) {
		File dir = StrToFile(dirStr);
		File[] files = dir.listFiles();
		String rootPath = dir.getPath();
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = files).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile1[i];
			if (file.isFile()) {
				String classFileName = file.getPath();
				if (classFileName.endsWith(".class")) {
					String tempClassName = classFileName.substring(
							rootPath.length() - className.lastIndexOf("."),
							classFileName.length() - ".class".length());
					if (className.equals(pathToDot(tempClassName))) {
						try {
							return this.fileClassLoader.loadClass(className);
						} catch (Exception ex) {
							logger.warn(
									"WARN ## load this class has an error,the fileName is = "
											+ className, ex);
						}
					}
				} else if (classFileName.endsWith(".jar")) {
					return scanInJar(classFileName, className);//TODO
				}
			} else if (file.isDirectory()) {
				Class<?> clz = findInDirectory(file.toString(), className);
				if (clz != null) {
					return clz;
				}
			}
		}

		return null;
	}

	private Class<?> scanInJar(String classFileName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	private File StrToFile(String dirString) {
		File file = new File(dirString);
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

	private String pathToDot(String s) {
		return s.replace('/', '.').replace('\\', '.');
	}

	public void setExtendsDir(String extendsDir) {
		this.extendsDir = extendsDir;

		File dir = new File(extendsDir);
		if (!dir.exists()) {
			try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				logger.error("##ERROR", e);
			}
		}
	}

	public void setFileClassLoader(FileSystemClassLoader fileClassLoader) {
		this.fileClassLoader = fileClassLoader;
	}
}