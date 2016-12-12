package cn.magicstudio.mblog.base.framework.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.yougou.logistics.base.common.utils.DataSourceSwitch;

public class DynamicDataSource extends AbstractRoutingDataSource {
	protected Object determineCurrentLookupKey() {
		return DataSourceSwitch.getCurrentdatasource();
	}
}
