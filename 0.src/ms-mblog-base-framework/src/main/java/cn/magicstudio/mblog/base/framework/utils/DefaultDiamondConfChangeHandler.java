package cn.magicstudio.mblog.base.framework.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDiamondConfChangeHandler extends DiamondConfChangeHandler {
	protected static final String DOT = ".";
	protected static final String SUFFIX = "properties";
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultDiamondConfChangeHandler.class);
	private boolean loder;

	public DefaultDiamondConfChangeHandler() {
		this.loder = true;
	}

	public DefaultDiamondConfChangeHandler(boolean loaderEnvironmentVariable) {
		this.loder = loaderEnvironmentVariable;
	}

	protected void afterHandler(String confPath, String propertyName,
			String content) {
		handlerFileGenerator(confPath, propertyName, content);
		handlerLoaderVariable(content);
	}

	private void handlerLoaderVariable(String content) {
		if (this.loder) {
			Properties p = new Properties();
			try {
				p.load(new StringReader(content));
			} catch (IOException e) {
				logger.error("实时加载配置失败,格式不规范，请检查", e);
			}
			if (p.size() > 0) {
				Properties temProps = System.getProperties();
				temProps.putAll(p);
				System.setProperties(temProps);
			}
		}
	}

	private void handlerFileGenerator(String confPath, String propName,
			String content) {
		try {
			File dir = new File(confPath);
			if ((!dir.exists()) || (!dir.isDirectory())) {
				logger.error("配置目录文件不存在");
				return;
			}

			File pFile = new File(dir, propName + "." + "properties");
			BufferedWriter w = new BufferedWriter(new FileWriter(pFile));
			if (pFile.exists()) {
				String c = IOUtils.toString(new FileReader(pFile));

				if (!StringUtils.equals(content, c)) {
					IOUtils.write(content, w);
					w.flush();
					w.close();
				}
			} else {
				IOUtils.write(content, w);
				w.flush();
				w.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("文件不存在", e);
		} catch (IOException e) {
			logger.error("读写出错", e);
		}
	}
}
