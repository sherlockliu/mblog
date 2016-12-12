package cn.magicstudio.mblog.base.framework.utils;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class StaticCache extends LocalCache {
	private static final Logger log = Logger.getLogger(StaticCache.class
			.getName());
	private final ConcurrentHashMap<String, CacheHolder> map;

	StaticCache(int initialSize) {
		this.map = new ConcurrentHashMap(initialSize, 0.75F);
	}

	void put(String key, Object value, long expire) {
		int i = 1000;
		CacheHolder c = new CacheHolder(value, expire
				+ System.currentTimeMillis());
		do {
			CacheHolder _c = (CacheHolder) this.map.get(key);
			if (_c == null) {
				_c = (CacheHolder) this.map.putIfAbsent(key, c);
				if (_c == null)
					break;
			} else {
				if ((_c.equals(value)) && (!_c.isExpired())) {
					return;
				}
				if (this.map.replace(key, _c, c))
					break;
			}
		} while (i-- != 0);
		log.log(Level.SEVERE, "CacheManage put value loop over 1000 times");
		this.stats.inserts.incrementAndGet();
		
		return;
	}

	Object get(String key) {
		if (null == key)
			throw new NullPointerException("cache key is null");
		CacheHolder c = (CacheHolder) this.map.get(key);
		this.stats.lookups.incrementAndGet();
		if ((c != null) && (!c.isExpired())) {
			this.stats.hits.incrementAndGet();
			return c.getValue();
		}
		return null;
	}

	Object getAndProlong(String key, long expire) {
		if (null == key)
			throw new NullPointerException("cache key is null");
		CacheHolder c = (CacheHolder) this.map.get(key);
		this.stats.lookups.incrementAndGet();
		if ((c != null) && (!c.isExpired())) {
			c.setTimeout(System.currentTimeMillis() + expire);
			this.stats.hits.incrementAndGet();
			return c.getValue();
		}
		return null;
	}

	void clear() {
		synchronized (this.map) {
			this.stats.evictions.addAndGet(size());
			this.map.clear();
		}
	}

	int size() {
		return this.map.size();
	}

	Object remove(String key) {
		CacheHolder c = (CacheHolder) this.map.get(key);
		if (null == c)
			return null;
		if (this.map.remove(key, c)) {
			this.stats.evictions.incrementAndGet();
			return c.getValue();
		}
		return null;
	}

	Set<String> keySet() {
		return new java.util.HashSet(this.map.keySet());
	}

	Object removeIfExpired(String key) {
		CacheHolder c = (CacheHolder) this.map.get(key);
		if (null == c)
			return null;
		if ((c.isExpired()) && (this.map.remove(key, c))) {
			this.stats.evictions.incrementAndGet();
			return c.getValue();
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
