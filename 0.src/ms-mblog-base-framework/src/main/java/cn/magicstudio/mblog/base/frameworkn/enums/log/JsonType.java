package cn.magicstudio.mblog.base.frameworkn.enums.log;

import cn.magicstudio.mblog.base.framework.enums.IndexEnum;

public enum JsonType implements IndexEnum<JsonType> {
	AUTO(0, "自动参数映射"), CUSTOM(1, "模板定制组装");

	private JsonType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	private String name;
	private int index;

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return this.name;
	}
}
