package cn.magicstudio.mblog.base.framework.utils;

import java.util.Set;

import cn.magicstudio.mblog.base.framework.enums.CacheStateEnum;


abstract class LocalCache {
	protected final CumulativeStats stats = new CumulativeStats();

	protected CacheStateEnum state;

	abstract boolean valideKey(String paramString);

	abstract void put(String paramString, Object paramObject, long paramLong);

	abstract Object get(String paramString);

	abstract Object getAndProlong(String paramString, long paramLong);

	abstract int size();

	abstract Object remove(String paramString);

	abstract Object removeIfExpired(String paramString);

	abstract Set<String> keySet();

	abstract void clear();

	abstract void clearIfExpired();

	CacheStateEnum getState() {
		return this.state;
	}

	CumulativeStats getStats() {
		return this.stats.clone();
	}
}
