package cn.magicstudio.mblog.monitor;

public abstract interface PerformancePublisher {
	public abstract void notify(Counter paramCounter);
}
