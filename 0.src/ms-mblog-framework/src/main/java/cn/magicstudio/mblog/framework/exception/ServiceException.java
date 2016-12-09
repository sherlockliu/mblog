package cn.magicstudio.mblog.framework.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 356381812673914209L;

	private int errorCode;

	public ServiceException() {
	}

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ServiceException(int code, String msg) {
		super(msg);
		errorCode = code;
	}

	public ServiceException(int code, String msg, Throwable cause) {
		super(msg, cause);
		errorCode = code;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
