package cn.magicstudio.mblog.cache.suggest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class IndexWriterManager implements BeanPostProcessor {
	private static final Map<String, SuggestIndexWriter> indexMap = new HashMap();

	private static final Logger logger = LoggerFactory
			.getLogger(IndexWriterManager.class);

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if ((bean instanceof SuggestIndexWriter)) {
			SuggestIndexWriter suggestIndexWriter = (SuggestIndexWriter) bean;
			String key = suggestIndexWriter.name();
			if (indexMap.get(key) == null) {
				indexMap.put(key, (SuggestIndexWriter) bean);
			} else {
				logger.warn("key:{} has been exist", key);
			}
		}
		return bean;
	}

	public SuggestIndexWriter getIndexWriter(String indexName) {
		return (SuggestIndexWriter) indexMap.get(indexName);
	}

	public Collection<SuggestIndexWriter> listWriters() {
		return indexMap.values();
	}
}
