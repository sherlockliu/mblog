package cn.magicstudio.mblog.cache;

import cn.wonhigh.retail.backend.model.EntityStatus;

public class EventAargs {
	private String type;
	private Object data;
	private EntityStatus status;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EntityStatus getStatus() {
		return this.status;
	}

	public void setStatus(EntityStatus type) {
		this.status = type;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
