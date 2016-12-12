package cn.magicstudio.mblog.extension;

import com.google.common.collect.MapMaker;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ExtensionMemoryMirror<KEY, VALUE> {
	private final Map<KEY, VALUE> store;
	private final ComputeFunction<KEY, VALUE> function;

	public ExtensionMemoryMirror(ComputeFunction<KEY, VALUE> function) {
		this.function = function;
		this.store = new MapMaker().makeMap();
	}

	public synchronized VALUE get(KEY key) {
		if (this.store.containsKey(key)) {
			KEY oldKey = null;
			for (KEY k : this.store.keySet()) {
				if (k.equals(key)) {
					oldKey = k;
					break;
				}
			}

			if (((Comparable) key).compareTo(oldKey) > 0) {
				VALUE result = this.function.apply(key);

				remove(key);
				put(key, result);
				return result;
			}
			return (VALUE) this.store.get(key);
		}

		VALUE result = this.function.apply(key);
		remove(key);
		put(key, result);
		return result;
	}

	public synchronized void put(KEY key, VALUE value) {
		this.store.put(getKey(key), value);
	}

	public synchronized void remove(Object key) {
		this.store.remove(getKey(key));
	}

	public synchronized void clear() {
		this.store.clear();
	}

	public Iterator getKeys() {
		return this.store.keySet().iterator();
	}

	public Set<Map.Entry<KEY, VALUE>> entrySet() {
		return this.store.entrySet();
	}

	public int size() {
		return this.store.size();
	}

	private KEY getKey(Object key) {
		if (key == null) {
			throw new IllegalArgumentException("Cache key not be null");
		}

		return (KEY) key;
	}

	public static abstract interface ComputeFunction<KEY, VALUE> {
		public abstract VALUE apply(KEY paramKEY);
	}
}
