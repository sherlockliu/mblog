package cn.magicstudio.mblog.base.framework.enums;

public enum OperationVerifyEnum {
	NONE(1), ADD(2), MODIFY(4), REMOVE(8), EXPORT(16), VERIFY(32), IGNORE(64);

	public final int bitNum;

	private OperationVerifyEnum(int bitNum) {
		this.bitNum = bitNum;
	}
}