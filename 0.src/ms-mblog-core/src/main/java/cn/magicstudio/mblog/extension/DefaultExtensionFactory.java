package cn.magicstudio.mblog.extension;

import cn.wonhigh.retail.backend.utils.dynamic.compile.ClassScanner;
import cn.wonhigh.retail.backend.utils.dynamic.compile.JavaSource;
import cn.wonhigh.retail.backend.utils.dynamic.compile.JdkCompiler;
import org.apache.commons.lang.StringUtils;

public class DefaultExtensionFactory implements ExtensionFactory {
	private ExtensionMemoryMirror<ExtensionData, Object> resolverCache;
	private ClassScanner classPathScanner;
	private ClassScanner fileSystemScanner;
	private JdkCompiler jdkCompiler;

	public DefaultExtensionFactory() {
		this.resolverCache = new ExtensionMemoryMirror(
				new ExtensionMemoryMirror.ComputeFunction() {
					public Object apply(ExtensionData extensionData) {
						return DefaultExtensionFactory.this
								.getExtensionInternal(extensionData);
					}

					@Override
					public Object apply(Object paramKEY) {
						// TODO Auto-generated method stub
						return null;
					}
				});
	}

	public <T> T getExtension(Class<T> type, ExtensionData extensionData) {
		return (T) this.resolverCache.get(extensionData);
	}

	private Object getExtensionInternal(ExtensionData extensionData) {
		Class<?> clazz = null;
		String fullname = "";

		if ((extensionData.getExtensionDataType().isClazz())
				&& (StringUtils.isNotBlank(extensionData.getClazzPath()))) {
			clazz = scan(extensionData.getClazzPath());
			fullname = "[" + extensionData.getClazzPath() + "]ClassPath";
		} else if ((extensionData.getExtensionDataType().isSource())
				&& (StringUtils.isNotBlank(extensionData.getSourceText()))) {
			JavaSource javaSource = new JavaSource(
					extensionData.getSourceText());
			clazz = this.jdkCompiler.compile(javaSource);
			fullname = "[" + javaSource.toString() + "]SourceText";
		}

		if (clazz == null) {
			throw new ExtensionLoadException(
					"ERROR ## classload this fileresolver=" + fullname
							+ " has an error");
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ExtensionLoadException(
					"ERROR ## classload this fileresolver=" + fullname
							+ " has an error", e);
		}
	}

	private Class scan(String fileResolverClassname) {
		Class<?> clazz = this.classPathScanner.scan(fileResolverClassname);
		if (clazz == null) {
			clazz = this.fileSystemScanner.scan(fileResolverClassname);
		}

		return clazz;
	}

	public void setClassPathScanner(ClassScanner classPathScanner) {
		this.classPathScanner = classPathScanner;
	}

	public void setFileSystemScanner(ClassScanner fileSystemScanner) {
		this.fileSystemScanner = fileSystemScanner;
	}

	public ClassScanner getFileSystemScanner() {
		return this.fileSystemScanner;
	}

	public void setJdkCompiler(JdkCompiler jdkCompiler) {
		this.jdkCompiler = jdkCompiler;
	}
}
