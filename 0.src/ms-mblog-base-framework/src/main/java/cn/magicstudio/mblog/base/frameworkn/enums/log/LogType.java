package cn.magicstudio.mblog.base.frameworkn.enums.log;

import cn.magicstudio.mblog.base.framework.enums.IndexEnum;

public enum LogType implements IndexEnum<LogType> {
	INSERT(0, "新增"), UPDATE(1, "更新"), DELETE(2, "删除"), SELECT(3, "查询"), MIX(4,
			"混合");

	private LogType(int index, String name) {
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
