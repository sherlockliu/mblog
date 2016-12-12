package cn.magicstudio.mblog.base.framework.enums;

public enum IdcIdentifyEnum {
	IDC_A("idc-a"), IDC_B("idc-b"), IDC_C("idc-c");

	private String identify;

	private IdcIdentifyEnum(String identify) {
		this.identify = identify;
	}

	public String getIdentify() {
		return this.identify;
	}
}
