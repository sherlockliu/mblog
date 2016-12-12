package cn.magicstudio.mblog.model;

public abstract interface SyncState {
	
	public abstract long getTimeSeq();

	public abstract void setTimeSeq(long paramLong);
}
