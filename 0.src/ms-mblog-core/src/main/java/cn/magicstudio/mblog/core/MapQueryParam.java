package cn.magicstudio.mblog.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapQueryParam<K, V> implements QueryParam<K, V> {
	private Map<K, V> map = new HashMap();

	public int size() {
		return this.map.size();
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	public V get(Object key) {
		return (V) this.map.get(key);
	}

	public V put(K key, V value) {
		return (V) this.map.put(key, value);
	}

	public V remove(Object key) {
		return (V) this.map.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		this.map.putAll(m);
	}

	public void clear() {
		this.map.clear();
	}

	public Set<K> keySet() {
		return this.map.keySet();
	}

	public java.util.Collection<V> values() {
		return this.map.values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.map.entrySet();
	}
}
