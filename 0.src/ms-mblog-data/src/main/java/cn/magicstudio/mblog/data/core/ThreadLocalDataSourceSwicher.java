package cn.magicstudio.mblog.data.core;

import org.apache.commons.lang.StringUtils;

public class ThreadLocalDataSourceSwicher implements DataSourceSwicher {
	protected static final ThreadLocal<String> contextHolder = new ThreadLocal();
	protected static final ThreadLocal<String> datasourceType = new ThreadLocal();

	public Object determineCurrentLookupKey() {
		String type = getDatasourceType();

		type = StringUtils.isEmpty(type) ? "" : type;

		return type + (String) contextHolder.get();
	}

	public void setCustomerType(String customerType) {
		contextHolder.set(customerType);
	}

	public String getDatasourceType() {
		return (String) datasourceType.get();
	}

	public void setDatasourceType(String val) {
		datasourceType.set(val);
	}
}