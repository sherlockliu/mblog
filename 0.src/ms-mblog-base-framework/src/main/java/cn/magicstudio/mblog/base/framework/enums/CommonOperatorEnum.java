package cn.magicstudio.mblog.base.framework.enums;

public enum CommonOperatorEnum {
	DELETED("deleted"), UPDATED("updated"), INSERTED("inserted");

	private String operator;

	private CommonOperatorEnum(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return this.operator;
	}
}
