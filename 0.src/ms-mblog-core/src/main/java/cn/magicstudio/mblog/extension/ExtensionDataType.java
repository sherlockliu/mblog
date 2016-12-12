package cn.magicstudio.mblog.extension;

public enum ExtensionDataType {
	CLAZZ, SOURCE;

	public boolean isClazz() {
		return equals(CLAZZ);
	}

	public boolean isSource() {
		return equals(SOURCE);
	}
}
