package cn.magicstudio.mblog.utils.dynamic.compile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class JdkCompileException extends Exception {
	private static final long serialVersionUID = 1L;
	private Set<String> classNames;
	private transient DiagnosticCollector<JavaFileObject> diagnostics;
	private String source;

	public JdkCompileException(String message, Set<String> qualifiedClassNames,
			Throwable cause, DiagnosticCollector<JavaFileObject> diagnostics) {
		super(message, cause);
		setClassNames(qualifiedClassNames);
		setDiagnostics(diagnostics);
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public JdkCompileException(String message, Set<String> qualifiedClassNames,
			DiagnosticCollector<JavaFileObject> diagnostics) {
		super(message);
		setClassNames(qualifiedClassNames);
		setDiagnostics(diagnostics);
	}

	public JdkCompileException(Set<String> qualifiedClassNames,
			Throwable cause, DiagnosticCollector<JavaFileObject> diagnostics) {
		super(cause);
		setClassNames(qualifiedClassNames);
		setDiagnostics(diagnostics);
	}

	private void setClassNames(Set<String> qualifiedClassNames) {
		this.classNames = new HashSet(qualifiedClassNames);
	}

	private void setDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics) {
		this.diagnostics = diagnostics;
	}

	public DiagnosticCollector<JavaFileObject> getDiagnostics() {
		return this.diagnostics;
	}

	public Collection<String> getClassNames() {
		return Collections.unmodifiableSet(this.classNames);
	}
}