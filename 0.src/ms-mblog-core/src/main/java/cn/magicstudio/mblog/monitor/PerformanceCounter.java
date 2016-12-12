package cn.magicstudio.mblog.monitor;

public abstract interface PerformanceCounter {
	public abstract void setName(String paramString);

	public abstract String getName();

	public abstract long increment(long paramLong);

	public abstract long total();

	public abstract long max();

	public abstract long min();

	public abstract double avg();

	public abstract double counting();

	public abstract AbstractPerformanceCounter.Counter info();

	public abstract void reset();
}