package cn.magicstudio.mblog.base.framework.vo;

public class SignCertVo {
	private String appCode;

	private String appKey;

	private String sign;

	private String signDate;

	public String getAppCode() {
		return this.appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignDate() {
		return this.signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
}
