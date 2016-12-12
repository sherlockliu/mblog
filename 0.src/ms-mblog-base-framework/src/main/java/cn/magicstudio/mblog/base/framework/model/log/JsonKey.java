package cn.magicstudio.mblog.base.framework.model.log;

import java.io.Serializable;

public class JsonKey implements Serializable {
	private static final long serialVersionUID = 8156801511036599773L;
	private String name;
	private String namecn;
	private ValueType valueType;
	private String desc;

	public static enum ValueType {
		STRING, NUMBER, BOOLEAN, NULL, OBJECT, ARRAY;

		private ValueType() {
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamecn() {
		return this.namecn;
	}

	public void setNamecn(String namecn) {
		this.namecn = namecn;
	}

	public ValueType getValueType() {
		return this.valueType;
	}

	public void setValueType(String valueType) {
		if (valueType.equalsIgnoreCase(ValueType.STRING.name())) {
			this.valueType = ValueType.STRING;
		} else if (valueType.equalsIgnoreCase(ValueType.NUMBER.name())) {
			this.valueType = ValueType.NUMBER;
		} else if (valueType.equalsIgnoreCase(ValueType.BOOLEAN.name())) {
			this.valueType = ValueType.BOOLEAN;
		} else if (valueType.equalsIgnoreCase(ValueType.NULL.name())) {
			this.valueType = ValueType.NULL;
		} else if (valueType.equalsIgnoreCase(ValueType.OBJECT.name())) {
			this.valueType = ValueType.OBJECT;
		} else if (valueType.equalsIgnoreCase(ValueType.ARRAY.name())) {
			this.valueType = ValueType.ARRAY;
		} else {
			this.valueType = null;
		}
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
