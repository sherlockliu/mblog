package cn.magicstudio.mblog.utils.dynamic.compile;

import java.util.ArrayList;
import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.apache.commons.lang.StringUtils;

public class JdkCompiler {
	private List<String> options;

	public JdkCompiler() {
		this.options = new ArrayList();
	}

	public Class compile(String sourceString) {
		JavaSource source = new JavaSource(sourceString);
		return compile(source);
	}

	public Class compile(JavaSource javaSource) {
		try {
			DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector();
			JdkCompileTask compileTask = new JdkCompileTask(
					new JdkCompilerClassLoader(getClass().getClassLoader()),
					this.options);
			String fullName = javaSource.getClassName();
			if (!StringUtils.isEmpty(javaSource.getPackageName()))
				fullName = javaSource.getPackageName() + "." + fullName;
			return compileTask.compile(fullName, javaSource.getSource(), errs);
		} catch (JdkCompileException ex) {
			DiagnosticCollector<JavaFileObject> diagnostics = ex
					.getDiagnostics();
			throw new CompileExprException("compile error, source : \n"
					+ javaSource + ", " + diagnostics.getDiagnostics(), ex);
		} catch (Exception ex) {
			throw new CompileExprException("compile error, source : \n"
					+ javaSource, ex);
		}
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}