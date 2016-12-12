package cn.magicstudio.mblog.base.frameworkn.enums.log;

import cn.magicstudio.mblog.base.framework.enums.IndexEnum;

public enum OperType implements IndexEnum<OperType> {
	USER(0, "用户"), SYSTEM(1, "系统");

	private OperType(int index, String name) {
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
