package cn.magicstudio.mblog.security;

import java.util.List;

public abstract interface DataAccessProvider {
	public abstract List<String> getAccessData(String paramString);

	public abstract String getAccessDataString(String paramString);

	public abstract void clear();
}
