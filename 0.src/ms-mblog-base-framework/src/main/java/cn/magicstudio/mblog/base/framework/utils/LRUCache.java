package cn.magicstudio.mblog.base.framework.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import cn.magicstudio.mblog.base.framework.enums.CacheStateEnum;


class LRUCache extends LocalCache {
	private Map<String, CacheHolder> map;

	LRUCache(final int maxSize, int initialSize) {
		initialSize = Math.min(maxSize, initialSize);
		this.map = Collections.synchronizedMap(new LinkedHashMap(initialSize,
				0.75F, true));
//		this.map = Collections.synchronizedMap(new LinkedHashMap(initialSize,
//				0.75F, true) {
//			protected boolean removeEldestEntry(
//					Map.Entry<String, CacheHolder> eldest) {
//				if (size() > maxSize) {
//					LRUCache.this.stats.evictions.incrementAndGet();
//					return true;
//				}
//				return false;
//			}
//		}); TODO
		this.state = CacheStateEnum.LRU;
	}

	public Object get(String key) {
		CacheHolder c = (CacheHolder) this.map.get(key);
		this.stats.lookups.incrementAndGet();
		if ((c != null) && (!c.isExpired())) {
			this.stats.hits.incrementAndGet();
			return c.getValue();
		}
		return null;
	}

	public Object getAndProlong(String key, long expire) {
		CacheHolder c = (CacheHolder) this.map.get(key);
		this.stats.lookups.incrementAndGet();
		if ((c != null) && (!c.isExpired())) {
			c.setTimeout(System.currentTimeMillis() + expire);
			this.stats.hits.incrementAndGet();
			return c.getValue();
		}
		return null;
	}

	public void put(String key, Object value, long expire) {
		this.stats.inserts.incrementAndGet();
		this.map.put(key,
				new CacheHolder(value, expire + System.currentTimeMillis()));
	}

	public void clear() {
		synchronized (this.map) {
			this.stats.inserts.addAndGet(size());
			this.map.clear();
		}
	}

	public int size() {
		return this.map.size();
	}

	Object remove(String key) {
		CacheHolder c = (CacheHolder) this.map.remove(key);
		if (null == c)
			return null;
		this.stats.evictions.incrementAndGet();
		return c.getValue();
	}

	Set<String> keySet() {
		return new HashSet(this.map.keySet());
	}

	CacheHolder removeIfExpired(String key) {
		synchronized (this.map) {
			CacheHolder c = (CacheHolder) this.map.get(key);
			if ((c != null) && (c.isExpired())) {
				this.stats.evictions.incrementAndGet();
				return (CacheHolder) this.map.remove(key);
			}
		}
		return null;
	}

	void clearIfExpired() {
		Iterator<String> keys = keySet().iterator();

		while (keys.hasNext()) {
			removeIfExpired((String) keys.next());
		}
	}

	boolean valideKey(String key) {
		CacheHolder c = (CacheHolder) this.map.get(key);
		this.stats.lookups.incrementAndGet();
		if ((null != c) && (!c.isExpired())) {
			this.stats.hits.incrementAndGet();
			return true;
		}
		return false;
	}
}
