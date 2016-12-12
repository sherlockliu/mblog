package cn.magicstudio.mblog.monitor;

import java.io.Serializable;
import java.util.Date;

public class Counter implements Serializable {
	private static final long serialVersionUID = 159894182460338603L;
	private String app;
	private Date date;
	private String type;
	private String uri;
	private String status;
	private long takes;
	private String tag;

	public String getApp() {
		return this.app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTakes() {
		return this.takes;
	}

	public void setTakes(long takes) {
		this.takes = takes;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
