package cn.magicstudio.mblog.monitor;

import java.util.concurrent.atomic.AtomicLong;

public class ValuePerformanceCounter extends AbstractPerformanceCounter {
	public ValuePerformanceCounter(String name) {
		super(name);
	}

	public long increment(long value) {
		return this.count.addAndGet(1L);
	}

	public long total() {
		return this.count.get();
	}
}