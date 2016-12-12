package cn.magicstudio.mblog.data.core;

import cn.wonhigh.retail.backend.utils.Helper;
import cn.wonhigh.retail.backend.utils.JsonUtils;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource implements
		InitializingBean, ApplicationContextAware {
	protected static final XLogger logger = XLoggerFactory
			.getXLogger(DynamicDataSource.class);

	public static final String DATASOURCE_TYPE_MYSQL = "mysql";

	public static final String DATASOURCE_TYPE_ORACLE = "oracle";
	private static DataSourceSwicher swicher;
	private String configFile;
	private String template;

	public void setSwicher(DataSourceSwicher value) {
		swicher = value;
	}

	public static void setCustomerType(String customerType) {
		swicher.setCustomerType(customerType);
	}

	public static Object getCustomerType() {
		return swicher.determineCurrentLookupKey();
	}

	public static void setDatasourceType(String val) {
		if (swicher != null) {
			swicher.setDatasourceType(val);
		}
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getConfigFile() {
		return this.configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	protected Object determineCurrentLookupKey() {
		Object val = getCustomerType();
		if (null == val) {
			return null;
		}
		String key = val.toString();
		Integer len = (Integer) datasourceCounter.get(key);

		if (null == len) {
			return key;
		}
		Long index = Long.valueOf(System.currentTimeMillis() % len.intValue());

		return key + "_" + index;
	}

	public void afterPropertiesSet() {
		Map<Object, Object> map = new HashMap();
		File file = new File(this.configFile);

		if (!file.exists()) {
			throw new NullPointerException("数据源配置文件不存在." + file);
		}
		try {
			if (file.exists()) {
				JsonNode nodes = JsonUtils.fromJson(Helper.readFile(file));
				for (JsonNode node : nodes) {
					loadDataSource(map, node);
				}
				setTargetDataSources(map);
			}
		} catch (Exception e) {
			logger.error("加载配置文件失败", e);
		}

		super.afterPropertiesSet();
	}

	private void loadDataSource(Map<Object, Object> map, JsonNode node) {
		List<String> jdbcUrls = new ArrayList();

		JsonNode jdbcUrlsNode = node.get("jdbcUrls");
		String url = node.get("jdbcUrl").asText();
		jdbcUrls.add(url);
		if ((jdbcUrlsNode != null) && (jdbcUrlsNode.isArray())) {
			ArrayNode nodes = (ArrayNode) jdbcUrlsNode;
			for (JsonNode item : nodes) {
				String val = item.asText();
				if (!url.equalsIgnoreCase(val)) {
					jdbcUrls.add(val);
				}
			}
		}
		String key = node.get("key").asText();
		String driverClass = node.get("driverClass").asText();
		String username = getValue(node, "username");
		String password = node.get("password").asText();
		String isDefault = getValue(node, "default");
		String type = getValue(node, "type");
		type = StringUtils.isNotEmpty(type) ? type + ":" : "";

		int i = 0;
		String[] vals = key.split(",");
		for (String txt : jdbcUrls) {
			DataSource ds = (DataSource) this.context.getBean(this.template);
			BeanWrapper wrapper = new BeanWrapperImpl(ds);
			PropertyDescriptor p = null;
			try {
				p = wrapper.getPropertyDescriptor("driverClassName");
			} catch (Exception e) {
			}

			if (p != null) {
				wrapper.setPropertyValue("url", txt);
				wrapper.setPropertyValue("driverClassName", driverClass);
			} else {
				wrapper.setPropertyValue("driverClass", driverClass);
				wrapper.setPropertyValue("jdbcUrl", txt);
			}

			wrapper.setPropertyValue("username", username);
			wrapper.setPropertyValue("password", password);

			if ((txt.equalsIgnoreCase(url))
					&& ("true".equalsIgnoreCase(isDefault))) {
				setDefaultTargetDataSource(ds);
			}

			for (String v : vals) {
				map.put(type + v + "_" + i, ds);
			}
			i++;
		}
		for (String v : vals) {
			datasourceCounter.put(type + v, Integer.valueOf(jdbcUrls.size()));
		}
	}

	private String getValue(JsonNode node, String name) {
		JsonNode n = node.get(name);
		if (n != null)
			return n.asText();
		return null;
	}

	private static Map<String, Integer> datasourceCounter = new HashMap();
	ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}
}