package cn.magicstudio.mblog.cache;

import cn.wonhigh.retail.backend.core.Storage;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component("redisStorage")
public class RedisStorage implements Storage {
	private static final String KEY_PREFIX = "";
	private long timeout = 1800L;
	private static RedisStorage current;

	public RedisStorage() {
		current = this;
	}

	public static RedisStorage getCurrent() {
		return current;
	}

	@Resource(name = "objRedisTemplate")
	private RedisTemplate<String, Object> objectTmpl;

	public void set(String key, Object value) {
		set(key, value, this.timeout);
	}

	public void set(String key, Object value, long expire) {
		String[] keys = key.split(",");
		if (keys.length == 1) {
			ValueOperations<String, Object> set = this.objectTmpl.opsForValue();
			key = key;
			set.set(key, value);
			this.objectTmpl.expire(key, expire, TimeUnit.SECONDS);
		} else if (keys.length == 2) {
			HashOperations<String, Object, Object> hset = this.objectTmpl
					.opsForHash();
			hset.put(keys[0], keys[1], value);
		} else {
			throw new UnknownError(key);
		}
	}

	public <T> T get(String key) {
		String[] keys = key.split(",");
		if (keys.length == 1) {
			ValueOperations<String, Object> set = this.objectTmpl.opsForValue();
			key = key;
			return (T) set.get(key);
		}
		if (keys.length == 2) {
			HashOperations<String, Object, Object> hset = this.objectTmpl
					.opsForHash();
			return (T) hset.get(keys[0], keys[1]);
		}
		throw new UnknownError(key);
	}

	public void remove(String key) {
		key = key;
		this.objectTmpl.delete(key);
	}
}
