package cn.magicstudio.mblog.base.framework.vo;

public class ResultVo {
	private int errorCode = 0;

	private String errorMessage = "";
	private Object data;

	public int getErrorCode() {
		return this.errorCode;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
