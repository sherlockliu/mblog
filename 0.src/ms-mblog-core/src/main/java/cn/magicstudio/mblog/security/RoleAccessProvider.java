package cn.magicstudio.mblog.security;

public abstract interface RoleAccessProvider {
	public abstract boolean hasPermission(String paramString);

	public abstract void clear();
}
