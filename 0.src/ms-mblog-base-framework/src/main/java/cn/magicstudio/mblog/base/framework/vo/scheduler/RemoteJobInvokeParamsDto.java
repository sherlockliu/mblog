package cn.magicstudio.mblog.base.framework.vo.scheduler;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RemoteJobInvokeParamsDto implements Serializable {
	private static final long serialVersionUID = 6839742419552816769L;
	protected Map<String, String> mapParams = new HashMap();

	public void addParam(String key, String value) {
		this.mapParams.put(key, value);
	}

	public String getParam(String key) {
		return (String) this.mapParams.get(key);
	}

	public void removeParam(String key) {
		this.mapParams.remove(key);
	}

	public Set<String> paramKeySet() {
		return this.mapParams.keySet();
	}

	public Collection<String> paramValues() {
		return this.mapParams.values();
	}

	public Set<Map.Entry<String, String>> paramEntrySet() {
		return this.mapParams.entrySet();
	}
}
