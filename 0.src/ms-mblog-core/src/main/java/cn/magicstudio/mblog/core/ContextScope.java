package cn.magicstudio.mblog.core;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class ContextScope implements AutoCloseable {
	static ThreadLocal<ContextScope> local = new ThreadLocal();
	public static AtomicLong counter = new AtomicLong(0L);
	Set<AutoCloseable> container = new java.util.HashSet();

	public ContextScope() {
		if (local.get() == null)
			local.set(this);
		counter.addAndGet(1L);
	}

	public static ContextScope current() {
		return (ContextScope) local.get();
	}

	public static void set(AutoCloseable item) {
		ContextScope scope = (ContextScope) local.get();
		if (scope == null)
			throw new NullPointerException("调用之前必须先实例化ContextScope.");
		scope.container.add(item);
	}

	public static void remove(AutoCloseable item) {
		ContextScope scope = (ContextScope) local.get();
		if (scope == null)
			throw new NullPointerException("调用之前必须先实例化ContextScope.");
		scope.container.remove(item);
	}

	public void close() throws Exception {
		local.remove();
		counter.addAndGet(-1L);
		for (AutoCloseable it : this.container) {
			try {
				it.close();
			} catch (Exception localException) {
			}
		}
	}
}
