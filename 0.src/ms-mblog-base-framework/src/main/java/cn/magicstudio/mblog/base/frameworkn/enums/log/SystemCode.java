package cn.magicstudio.mblog.base.frameworkn.enums.log;

import cn.magicstudio.mblog.base.framework.enums.IndexEnum;

public enum SystemCode implements IndexEnum<SystemCode> {
	UC(0, "用户中心"), MDM(31, "基础数据"), GMS(33, "货品管理"), PMS(32, "订货会"), FAS(30,
			"财务辅助"), POS(35, "POS销售"), MPS(34, "营促销"), OC(36, "订单中心"), CRM(38,
			"客户管理");

	private SystemCode(int index, String name) {
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
