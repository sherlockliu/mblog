package cn.magicstudio.mblog.utils.dynamic.compile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

public final class JavaFileObjectImpl extends SimpleJavaFileObject {
	private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

	private final CharSequence source;

	public JavaFileObjectImpl(String baseName, CharSequence source) {
		super(JdkCompileTask.toURI(baseName + ".java"),
				JavaFileObject.Kind.SOURCE);
		this.source = source;
	}

	public JavaFileObjectImpl(String name, JavaFileObject.Kind kind) {
		super(JdkCompileTask.toURI(name), kind);
		this.source = null;
	}

	public JavaFileObjectImpl(URI uri, JavaFileObject.Kind kind) {
		super(uri, kind);
		this.source = null;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws UnsupportedOperationException {
		if (this.source == null) {
			throw new UnsupportedOperationException();
		}

		return this.source;
	}

	public InputStream openInputStream() {
		return new ByteArrayInputStream(getByteCode());
	}

	public OutputStream openOutputStream() {
		return this.byteCode;
	}

	public byte[] getByteCode() {
		return this.byteCode.toByteArray();
	}
}