package cn.magicstudio.mblog.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.NamedThreadLocal;

public class ApplicationContext {
	private static NamedThreadLocal<ApplicationContext> context = new NamedThreadLocal(
			"core.context");

	public static String host;
	private Map<String, Object> map = new HashMap();
	public static ApplicationContext current() {
		ApplicationContext app = (ApplicationContext) context.get();
		if (app == null) {
			app = new ApplicationContext();
			context.set(app);
		}
		return app;
	}

	public void dispose() {
		try {
			context.remove();
		} catch (Exception localException) {
		}
	}

	public void clear() {
		this.map.clear();
	}

	public <T> T getValue(String key) {
		if (this.map.containsKey(key))
			return (T) this.map.get(key);
		return null;
	}

	public void setValue(String key, Object value) {
		this.map.put(key, value);
	}

	public static String getAppCode() {
		int id = Integer.parseInt(System.getProperty("system.id", "0"));
		if (id == 12)
			return "gms";
		if (id == 16)
			return "mdm";
		if (id == 14)
			return "fas";
		if (id == 10) {
			return "pms";
		}
		return "unknow";
	}
}
