package cn.magicstudio.mblog.base.framework.utils;

import java.io.Serializable;

class CacheHolder implements Serializable {
	private long timeout;
	private final Object value;
	private volatile boolean expiredFlag;

	public CacheHolder(Object value, long timeout) {
		this.value = value;
		this.timeout = timeout;
	}

	public Object getValue() {
		return this.value;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isExpired() {
		if (!this.expiredFlag)
			this.expiredFlag = (System.currentTimeMillis() > this.timeout);
		return this.expiredFlag;
	}

	public boolean equals(Object o) {
		if ((o == null) || (!(o instanceof CacheHolder)))
			return false;
		CacheHolder _o = (CacheHolder) o;
		if ((null == this.value) || (isExpired()))
			return (null == _o.getValue()) || (_o.isExpired());
		return this.value.equals(_o.getValue());
	}
}
