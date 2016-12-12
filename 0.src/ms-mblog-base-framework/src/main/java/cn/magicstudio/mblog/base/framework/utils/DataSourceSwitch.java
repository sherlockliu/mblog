package cn.magicstudio.mblog.base.framework.utils;

public class DataSourceSwitch {
	private static final ThreadLocal<String> _currentDataSource = new ThreadLocal();

	public static void setCurrentDataSource(String dataSourceName) {
		_currentDataSource.set(dataSourceName);
	}

	public static String getCurrentdatasource() {
		return (String) _currentDataSource.get();
	}
}
