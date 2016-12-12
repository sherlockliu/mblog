package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.tools.JavaFileObject;

public final class JdkCompilerClassLoader extends ClassLoader {
	private final Map<String, JavaFileObject> classes = new HashMap();

	public JdkCompilerClassLoader(ClassLoader parentClassLoader) {
		super(parentClassLoader);
	}

	public Collection<JavaFileObject> files() {
		return Collections.unmodifiableCollection(this.classes.values());
	}

	public void clearCache() {
		this.classes.clear();
	}

	public JavaFileObject getJavaFileObject(String qualifiedClassName) {
		return (JavaFileObject) this.classes.get(qualifiedClassName);
	}

	protected synchronized Class<?> findClass(String qualifiedClassName)
			throws ClassNotFoundException {
		JavaFileObject file = (JavaFileObject) this.classes
				.get(qualifiedClassName);
		if (file != null) {
			byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
			return defineClass(qualifiedClassName, bytes, 0, bytes.length);
		}
		try {
			return Class.forName(qualifiedClassName);
		} catch (ClassNotFoundException localClassNotFoundException) {
			try {
				return Thread.currentThread().getContextClassLoader()
						.loadClass(qualifiedClassName);
			} catch (ClassNotFoundException localClassNotFoundException1) {
			}
		}

		return super.findClass(qualifiedClassName);
	}

	public void add(String qualifiedClassName, JavaFileObject javaFile) {
		this.classes.put(qualifiedClassName, javaFile);
	}

	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		try {
			Class c = findClass(name);
			if (c != null) {
				if (resolve) {
					resolveClass(c);
				}

				return c;
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
		}

		return super.loadClass(name, resolve);
	}

	public InputStream getResourceAsStream(String name) {
		if (name.endsWith(".class")) {
			String qualifiedClassName = name.substring(0,
					name.length() - ".class".length()).replace('/', '.');
			JavaFileObjectImpl file = (JavaFileObjectImpl) this.classes
					.get(qualifiedClassName);

			if (file != null) {
				return new ByteArrayInputStream(file.getByteCode());
			}
		}

		return super.getResourceAsStream(name);
	}
}