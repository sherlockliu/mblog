package cn.magicstudio.mblog.monitor;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class PerformanceManager {
	private boolean enable = true;
	private static PerformanceManager instance;

	public PerformanceManager() {
		instance = this;
	}

	public static PerformanceManager getInstance() {
		if (instance == null)
			instance = new PerformanceManager();
		return instance;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	private Map<String, PerformanceCounter> counters = new Hashtable();

	public synchronized void addCouter(PerformanceCounter counter) {
		this.counters.put(counter.getName(), counter);
	}

	public Collection<PerformanceCounter> getCounters() {
		return this.counters.values();
	}

	public PerformanceCounter getCounter(String name) {
		if (this.counters.containsKey(name))
			return (PerformanceCounter) this.counters.get(name);
		synchronized (name.intern()) {
			if (this.counters.containsKey(name)) {
				return (PerformanceCounter) this.counters.get(name);
			}
			AbstractPerformanceCounter counter = new AbstractPerformanceCounter(
					name);
			this.counters.put(name, counter);
			return counter;
		}
	}
}