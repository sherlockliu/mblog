package cn.magicstudio.mblog.base.framework.utils;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class CumulativeStats implements Cloneable, Serializable {
	AtomicLong lookups = new AtomicLong();

	AtomicLong hits = new AtomicLong();

	AtomicLong inserts = new AtomicLong();

	AtomicLong evictions = new AtomicLong();

	public CumulativeStats clone() {
		CumulativeStats _stats = new CumulativeStats();
		_stats.lookups.set(this.lookups.longValue());
		_stats.hits.set(this.hits.longValue());
		_stats.inserts.set(this.inserts.longValue());
		_stats.evictions.set(this.evictions.longValue());
		return _stats;
	}

	public AtomicLong getLookups() {
		return this.lookups;
	}

	public AtomicLong getHits() {
		return this.hits;
	}

	public AtomicLong getInserts() {
		return this.inserts;
	}

	public AtomicLong getEvictions() {
		return this.evictions;
	}
}
