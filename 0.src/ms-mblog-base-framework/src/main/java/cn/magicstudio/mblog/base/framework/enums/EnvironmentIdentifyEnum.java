package cn.magicstudio.mblog.base.framework.enums;

public enum EnvironmentIdentifyEnum {
	DEV("dev"),

	TEST("test"),

	EXPERIENCE("experience"),

	ONLINE("online"),

	TRAIN("train");

	private String identify;

	private EnvironmentIdentifyEnum(String identify) {
		this.identify = identify;
	}

	public String getIdentify() {
		return this.identify;
	}
}
