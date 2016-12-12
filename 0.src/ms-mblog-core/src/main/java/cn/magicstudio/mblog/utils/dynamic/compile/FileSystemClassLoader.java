package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemClassLoader extends ClassLoader {
	private static final Logger logger = LoggerFactory
			.getLogger(FileSystemClassLoader.class);
	private String rootDir;

	public FileSystemClassLoader(String rootDir, ClassLoader parent) {
		super(parent);
		this.rootDir = rootDir;
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = getClassData(name);
		if (classData == null) {
			throw new ClassNotFoundException();
		}
		return defineClass(name, classData, 0, classData.length);
	}

	private byte[] getClassData(String className) {
		String path = classNameToPath(className);
		InputStream ins = null;
		try {
			ins = new FileInputStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bufferSize = 4096;
			byte[] buffer = new byte[bufferSize];
			int bytesNumRead = 0;
			while ((bytesNumRead = ins.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesNumRead);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			logger.error("ERROR ## get class data has an error", e);
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					logger.error("ERROR ## close inputstream has an error", e);
				}
			}
		}
		return null;
	}

	private String classNameToPath(String className) {
		return this.rootDir + File.separatorChar
				+ className.replace('.', File.separatorChar) + ".class";
	}
}