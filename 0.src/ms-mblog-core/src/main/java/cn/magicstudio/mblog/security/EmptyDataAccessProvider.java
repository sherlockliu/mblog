package cn.magicstudio.mblog.security;

import java.util.List;

public class EmptyDataAccessProvider implements DataAccessProvider {
	public List<String> getAccessData(String name) {
		return null;
	}

	public String getAccessDataString(String name) {
		return null;
	}

	public void clear() {
	}
}
