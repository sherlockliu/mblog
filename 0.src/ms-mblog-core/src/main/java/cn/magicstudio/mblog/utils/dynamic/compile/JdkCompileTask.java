package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JdkCompileTask<T> {
	public static final String JAVA_EXTENSION = ".java";
	private final JdkCompilerClassLoader classLoader;
	private final JavaCompiler compiler;
	private final List<String> options;
	private DiagnosticCollector<JavaFileObject> diagnostics;
	private JavaFileManagerImpl javaFileManager;

	public JdkCompileTask(JdkCompilerClassLoader classLoader,
			Iterable<String> options) {
		this.compiler = ToolProvider.getSystemJavaCompiler();
		if (this.compiler == null) {
			throw new IllegalStateException(
					"Cannot find the system Java compiler. Check that your class path includes tools.jar");
		}

		this.classLoader = classLoader;
		ClassLoader loader = classLoader.getParent();
		this.diagnostics = new DiagnosticCollector();
		StandardJavaFileManager fileManager = this.compiler
				.getStandardFileManager(this.diagnostics, null, null);
		List<File> path;
		if (((loader instanceof URLClassLoader))
				&& (!loader.getClass().getName()
						.equalsIgnoreCase("sun.misc.Launcher$AppClassLoader"))) {
			try {
				URLClassLoader urlClassLoader = (URLClassLoader) loader;

				path = new ArrayList();
				URL[] arrayOfURL;
				int j = (arrayOfURL = urlClassLoader.getURLs()).length;
				for (int i = 0; i < j; i++) {
					URL url = arrayOfURL[i];
					File file = new File(url.getFile());
					path.add(file);
				}

				fileManager.setLocation(StandardLocation.CLASS_PATH, path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		this.javaFileManager = new JavaFileManagerImpl(fileManager, classLoader);
		this.options = new ArrayList();
		if (options != null) {
			for (String option : options) {
				this.options.add(option);
			}
		}
	}

	public synchronized Class compile(String className,
			CharSequence javaSource,
			DiagnosticCollector<JavaFileObject> diagnosticsList)
			throws JdkCompileException, ClassCastException {
		if (diagnosticsList != null) {
			this.diagnostics = diagnosticsList;
		} else {
			this.diagnostics = new DiagnosticCollector();
		}

		Map<String, CharSequence> classes = new HashMap(1);
		classes.put(className, javaSource);

		Map<String, Class> compiled = compile(classes, diagnosticsList);
		Class newClass = (Class) compiled.get(className);
		return newClass;
	}

	public synchronized Map<String, Class> compile(
			Map<String, CharSequence> classes,
			DiagnosticCollector<JavaFileObject> diagnosticsList)
			throws JdkCompileException {
		Map<String, Class> compiled = new HashMap();

		List<JavaFileObject> sources = new ArrayList();
		CharSequence javaSource;
		for (Map.Entry<String, CharSequence> entry : classes.entrySet()) {
			String qualifiedClassName = (String) entry.getKey();
			javaSource = (CharSequence) entry.getValue();
			if (javaSource != null) {
				int dotPos = qualifiedClassName.lastIndexOf('.');
				String className = dotPos == -1 ? qualifiedClassName
						: qualifiedClassName.substring(dotPos + 1);
				String packageName = dotPos == -1 ? "" : qualifiedClassName
						.substring(0, dotPos);
				JavaFileObjectImpl source = new JavaFileObjectImpl(className,
						javaSource);
				sources.add(source);
				this.javaFileManager.putFileForInput(
						StandardLocation.SOURCE_PATH, packageName, className
								+ ".java", source);
			}
		}

		JavaCompiler.CompilationTask task = this.compiler.getTask(null,
				this.javaFileManager, this.diagnostics, this.options, null,
				sources);
		Boolean result = task.call();
		if ((result == null) || (!result.booleanValue())) {
			throw new JdkCompileException("Compilation failed.",
					classes.keySet(), this.diagnostics);
		}

		try {
			for (String qualifiedClassName : classes.keySet()) {
				Class<T> newClass = loadClass(qualifiedClassName);
				compiled.put(qualifiedClassName, newClass);
			}

			return compiled;
		} catch (ClassNotFoundException e) {
			throw new JdkCompileException(classes.keySet(), e, this.diagnostics);
		} catch (IllegalArgumentException e) {
			throw new JdkCompileException(classes.keySet(), e, this.diagnostics);
		} catch (SecurityException e) {
			throw new JdkCompileException(classes.keySet(), e, this.diagnostics);
		}
	}

	public Class<T> loadClass(String qualifiedClassName)
			throws ClassNotFoundException {
//		return this.classLoader.loadClass(qualifiedClassName); TODO
		return null;
	}

	public static URI toURI(String name) {
		try {
			return new URI(name);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public ClassLoader getClassLoader() {
		return this.javaFileManager.getClassLoader();
	}
}
