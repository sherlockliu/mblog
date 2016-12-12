package cn.magicstudio.mblog.extension;

import com.google.common.collect.MapMaker;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;

public class RefreshMemoryMirror<KEY, VALUE> {
	private final Long period;
	private final Map<String, RefreshObject<VALUE>> store;
	private final ComputeFunction<KEY, VALUE> function;

	public RefreshMemoryMirror(Long period, ComputeFunction<KEY, VALUE> function) {
		this.period = period;
		this.function = function;
		this.store = new MapMaker().makeMap();
	}

	public synchronized VALUE get(KEY key) {
		RefreshObject<VALUE> object = (RefreshObject) this.store
				.get(getKey(key));

		if (object == null) {
			VALUE result = this.function.apply(key, null);
			put(key, result);
			return result;
		}
		if (isExpired(object)) {
			VALUE result = this.function.apply(key, object.getValue());
			put(key, result);
			return result;
		}
		return (VALUE) object.getValue();
	}

	public synchronized void put(KEY key, VALUE value) {
		RefreshObject<VALUE> object = new RefreshObject(value);
		this.store.put(getKey(key), object);
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

	private boolean isExpired(RefreshObject refreshObject) {
		if (refreshObject == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(refreshObject.getTimestamp());

		calendar.add(14, this.period.intValue());

		Date now = new Date();
		return now.after(calendar.getTime());
	}

	private String getKey(Object key) {
		if (key == null) {
			throw new IllegalArgumentException("Cache key not be null");
		}

		return ObjectUtils.toString(key);
	}

	public String toString() {
		return "RefreshMemoryCache[period=" + this.period + ", size="
				+ this.store.size() + "]";
	}

	public static abstract interface ComputeFunction<KEY, VALUE> {
		public abstract VALUE apply(KEY paramKEY, VALUE paramVALUE);
	}

	public static class RefreshObject<VALUE> {
		private long timestamp;

		private VALUE value;

		public RefreshObject(VALUE value) {
			this.value = value;
			this.timestamp = new Date().getTime();
		}

		public VALUE getValue() {
			return (VALUE) this.value;
		}

		public void setValue(VALUE value) {
			this.value = value;
		}

		public long getTimestamp() {
			return this.timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}
