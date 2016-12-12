package cn.magicstudio.mblog.base.framework.model;

import java.io.Serializable;

public class CookieModel implements Serializable {
	private static final long serialVersionUID = -2914113287216095636L;
	private String key;
	private String value;
	private String domain;
	private String path;
	private String expiryKey;
	private long expiry;

	public CookieModel(String key, String value, String domain, String path,
			long expiry) {
		this.key = key;
		this.value = value;
		this.domain = domain;
		this.path = path;
		this.expiry = expiry;
	}

	public CookieModel(String key, String value, String domain, String path,
			long expiry, String expiryKey) {
		this.key = key;
		this.value = value;
		this.domain = domain;
		this.path = path;
		this.expiryKey = expiryKey;
		this.expiry = expiry;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExpiryKey() {
		return this.expiryKey;
	}

	public void setExpiryKey(String expiryKey) {
		this.expiryKey = expiryKey;
	}

	public long getExpiry() {
		return this.expiry;
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}
}
