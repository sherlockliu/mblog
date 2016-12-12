package cn.magicstudio.mblog.cache.suggest.redis;

import cn.magicstudio.mblog.cache.suggest.IndexReader;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractRedisIndexReader<T> implements IndexReader<T> {
	@Resource
	protected RedisConnectionFactory redisConnectionFactory;
	protected RedisTemplate<String, String> stringTmpl;

	public List<T> search(String word, int skip, int take,
			Map<String, Object> condition) {
		throw new RuntimeException("not impl");
	}
}
