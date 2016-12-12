package cn.magicstudio.mblog.extension;

public class ExtensionLoadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExtensionLoadException(String cause) {
		super(cause);
	}

	public ExtensionLoadException(Throwable t) {
		super(t);
	}

	public ExtensionLoadException(String cause, Throwable t) {
		super(cause, t);
	}
}
