package cn.magicstudio.mblog.cache.suggest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("search")
public class Search implements ApplicationContextAware, AutoCloseable {
	private int defaultTake = 10;

	Map<String, IndexReader> readers = new HashMap();
	private boolean cached = false;

	static Cache<String, Object> cache = null;

	protected final void initialize() {
	}

	public synchronized void clear(String type, String key, String bean) {
		if ((StringUtils.isEmpty(type)) && (StringUtils.isEmpty(key))) {
			cache.invalidateAll();
		} else if ((StringUtils.isNotEmpty(type))
				&& (StringUtils.isNotEmpty(key))) {
			String s = type + "_" + key;
			if (StringUtils.isNotEmpty(bean))
				s = s + "_" + bean;
			cache.invalidate(s);
		}
	}

	static <K, V> Cache<K, V> callableCached() {
		Cache<K, V> cache = CacheBuilder.newBuilder().maximumSize(50000L)
				.expireAfterWrite(45L, TimeUnit.MINUTES).build();
		return cache;
	}

	public Search open() {
		Search search = new Search();
		search.readers = this.readers;
		search.cached = true;

		return search;
	}

	public <T> List<T> search(String type, String word) {
		return search(type, word, 0, this.defaultTake);
	}

	public <T> List<T> get(String type, String key) {
		return get(type, key, null);
	}

	public <T> T getSingle(String type, String key) {
		return (T) getSingle(type, key, null);
	}

	public <T> T getSingle(String type, String key, Map<String, Object> params) {
		List<T> lst = get(type, key, params);
		if ((lst == null) || (lst.size() == 0))
			return null;
		return (T) lst.get(0);
	}

	public <T> List<T> get(String type, String key, Map<String, Object> params) {
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(type))) {
			return null;
		}
		String s = type + "_" + key;
		if ((params != null) && (params.containsKey("bean"))) {
			s = s + "_" + params.get("bean");
		}
		List<T> result = (List) cache.getIfPresent(s);
		if ((this.cached) && (result != null) && (result.size() > 0)) {
			return result;
		}
		if (params == null) {
			params = new HashMap();
		}
		IndexReader<T> reader = (IndexReader) this.readers.get(type);
		if (reader == null) {
			reader = (IndexReader) this.readers.get("default");
			params.put("bean", type);
		}
		result = reader.get(key, params);
		if ((this.cached) && (result != null))
			cache.put(s, result);
		return result;
	}

	public <T> List<T> search(String type, String word, int skip, int take) {
		if (!this.readers.containsKey(type)) {
			return null;
		}
		IndexReader<T> reader = (IndexReader) this.readers.get(type);
		return reader.search(word, skip, take);
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		Map<String, IndexReader> items = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(context, IndexReader.class,
						false, false);

		for (Map.Entry<String, IndexReader> kv : items.entrySet()) {
			IndexReader<?> item = (IndexReader) kv.getValue();
			String typeName = item.name();
			this.readers.put(typeName.toLowerCase(), item);
		}
	}

	public void close() throws Exception {
	}
}
