package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

public class JavaFileManagerImpl extends
		ForwardingJavaFileManager<JavaFileManager> {
	private final JdkCompilerClassLoader classLoader;
	private final Map<URI, JavaFileObject> fileObjects = new HashMap();

	public JavaFileManagerImpl(JavaFileManager fileManager,
			JdkCompilerClassLoader classLoader) {
		super(fileManager);
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public FileObject getFileForInput(JavaFileManager.Location location,
			String packageName, String relativeName) throws IOException {
		FileObject o = (FileObject) this.fileObjects.get(uri(location,
				packageName, relativeName));

		if (o != null) {
			return o;
		}

		return super.getFileForInput(location, packageName, relativeName);
	}

	public void putFileForInput(StandardLocation location, String packageName,
			String relativeName, JavaFileObject file) {
		this.fileObjects.put(uri(location, packageName, relativeName), file);
	}

	private URI uri(JavaFileManager.Location location, String packageName,
			String relativeName) {
		return JdkCompileTask.toURI(location.getName() + '/' + packageName
				+ '/' + relativeName);
	}

	public JavaFileObject getJavaFileForOutput(
			JavaFileManager.Location location, String qualifiedName,
			JavaFileObject.Kind kind, FileObject outputFile) throws IOException {
		JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
		this.classLoader.add(qualifiedName, file);
		return file;
	}

	public ClassLoader getClassLoader(JavaFileManager.Location location) {
		return this.classLoader;
	}

	public String inferBinaryName(JavaFileManager.Location loc,
			JavaFileObject file) {
		if ((file instanceof JavaFileObjectImpl)) {
			return file.getName();
		}

		return super.inferBinaryName(loc, file);
	}

	public Iterable<JavaFileObject> list(JavaFileManager.Location location,
			String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
			throws IOException {
		Iterable<JavaFileObject> result = super.list(location, packageName,
				kinds, recurse);

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		List<URL> urlList = new ArrayList();
		Enumeration<URL> e = contextClassLoader.getResources("com");
		while (e.hasMoreElements()) {
			urlList.add((URL) e.nextElement());
		}

		ArrayList<JavaFileObject> files = new ArrayList();

		if ((location == StandardLocation.CLASS_PATH)
				&& (kinds.contains(JavaFileObject.Kind.CLASS))) {
			for (JavaFileObject file : this.fileObjects.values()) {
				if ((file.getKind() == JavaFileObject.Kind.CLASS)
						&& (file.getName().startsWith(packageName))) {
					files.add(file);
				}
			}

			files.addAll(this.classLoader.files());
		} else if ((location == StandardLocation.SOURCE_PATH)
				&& (kinds.contains(JavaFileObject.Kind.SOURCE))) {
			for (JavaFileObject file : this.fileObjects.values()) {
				if ((file.getKind() == JavaFileObject.Kind.SOURCE)
						&& (file.getName().startsWith(packageName))) {
					files.add(file);
				}
			}
		}

		for (JavaFileObject file : result) {
			files.add(file);
		}

		return files;
	}
}