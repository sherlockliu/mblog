package cn.magicstudio.mblog.model;

import java.io.Serializable;

public abstract class BaseEntity implements SyncState, IKey, Serializable {
	long timeSeq;
	private static final long serialVersionUID = -5339621148334035518L;

	public long getTimeSeq() {
		return this.timeSeq;
	}

	public void setTimeSeq(long timeSeq) {
		this.timeSeq = timeSeq;
	}

	public void setKey(Object val) {
	}

	public Object getKey() {
		return null;
	}

	public String getCode() {
		return null;
	}

	public String getName() {
		return null;
	}
}
