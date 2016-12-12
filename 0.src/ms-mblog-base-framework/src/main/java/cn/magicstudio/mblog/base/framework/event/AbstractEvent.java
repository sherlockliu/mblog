package cn.magicstudio.mblog.base.framework.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEvent<TYPE extends Enum<TYPE>> implements
		IEvent<TYPE> {
	protected TYPE type;
	protected Object target;
	protected Object data;
	protected Map<String, Object> params;

	public AbstractEvent() {
		this.type = null;
	}

	public AbstractEvent(TYPE type) {
		this.type = type;
	}

	public AbstractEvent(TYPE type, Object target, Object data) {
		this.type = type;
		this.target = target;
		this.data = data;
	}

	public Object getTarget() {
		return this.target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getParameter(String key) {
		Object param = null;
		if (this.params != null) {
			param = this.params.get(key);
		}
		return param;
	}

	public void setParameter(String key, Object value) {
		if (this.params == null) {
			this.params = new ConcurrentHashMap();
		}
		this.params.put(key, value);
	}

	public String toString() {
		return "Event { Type:" + this.type + ", Source: " + this.target
				+ ", Data: " + this.data + ", Params: " + this.params + " }";
	}

	public TYPE getType() {
		return this.type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}
}
