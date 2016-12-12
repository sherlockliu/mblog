package cn.magicstudio.mblog.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

public class RedisTemplateExt<K, V> {
	public static final long DEFAULT_TIMEOUT = 60L;
	public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.MINUTES;
	private RedisTemplate<K, V> redisTemplate;

	public RedisTemplateExt(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<K, V> getTemplate() {
		return this.redisTemplate;
	}

	public void setDefaultSerializer(RedisSerializer<?> serializer) {
		getTemplate().setDefaultSerializer(serializer);
	}

	public void setKeySerializer(RedisSerializer<?> serializer) {
		getTemplate().setKeySerializer(serializer);
	}

	public RedisSerializer<?> getKeySerializer() {
		return getTemplate().getKeySerializer();
	}

	public void setValueSerializer(RedisSerializer<?> serializer) {
		getTemplate().setValueSerializer(serializer);
	}

	public RedisSerializer<?> getValueSerializer() {
		return getTemplate().getValueSerializer();
	}

	public RedisSerializer<?> getHashKeySerializer() {
		return getTemplate().getHashKeySerializer();
	}

	public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
		getTemplate().setHashKeySerializer(hashKeySerializer);
	}

	public RedisSerializer<?> getHashValueSerializer() {
		return getTemplate().getHashValueSerializer();
	}

	public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
		getTemplate().setHashValueSerializer(hashValueSerializer);
	}

	public RedisSerializer<String> getStringSerializer() {
		return getTemplate().getStringSerializer();
	}

	public void setStringSerializer(RedisSerializer<String> stringSerializer) {
		getTemplate().setStringSerializer(stringSerializer);
	}

	public <T> T exec(RedisCallback<T> callbak) {
		return (T) getTemplate().execute(callbak);
	}

	public <T> T exec(SessionCallback<T> callbak) {
		return (T) getTemplate().execute(callbak);
	}

	public boolean expire(K key, long timeout, TimeUnit unit) {
		return getTemplate().expire(key, timeout, unit).booleanValue();
	}

	public boolean expire(K key, long timeout) {
		return expire(key, timeout, TimeUnit.SECONDS);
	}

	public boolean exists(K key) {
		return getTemplate().hasKey(key).booleanValue();
	}

	public void del(K key) {
		getTemplate().delete(key);
	}

	public void del(Set<K> keys) {
		getTemplate().delete(keys);
	}

	public void rename(K oldKey, K newKey) {
		getTemplate().rename(oldKey, newKey);
	}

	public void set(K key, V value) {
		set(key, value, 60L);
	}

	public void set(K key, V value, long timeout) {
		set(key, value, timeout, DEFAULT_TIMEUNIT);
	}

	public void set(K key, V value, long timeout, TimeUnit timeUnit) {
		getTemplate().opsForValue().set(key, value, timeout, timeUnit);
	}

	public void mset(Map<? extends K, V> m) {
		getTemplate().opsForValue().multiSet(m);
	}

	public void append(K key, String value) {
		getTemplate().opsForValue().append(key, value);
	}

	public V get(K key) {
		return (V) getTemplate().opsForValue().get(key);
	}

	public List<V> mget(Collection<K> keys) {
		return getTemplate().opsForValue().multiGet(keys);
	}

	public V getSet(K key, V newValue) {
		return (V) getTemplate().opsForValue().getAndSet(key, newValue);
	}

	public long strLen(K key) {
		return getTemplate().opsForValue().size(key).longValue();
	}

	public long incr(K key) {
		return incr(key, 1L);
	}

	public long incr(K key, long num) {
		return getTemplate().opsForValue().increment(key, num).longValue();
	}

	public long decr(K key) {
		return decr(key, 1L);
	}

	public long decr(K key, long num) {
		return getTemplate().opsForValue().increment(key, -1L * num)
				.longValue();
	}

	public long lLen(K key) {
		return getTemplate().opsForList().size(key).longValue();
	}

	public List<V> lAll(K key) {
		return getTemplate().opsForList().range(key, 0L, lLen(key));
	}

	public void lPush(K key, V value) {
		getTemplate().opsForList().leftPush(key, value);
	}

	public void rPush(K key, V value) {
		getTemplate().opsForList().rightPush(key, value);
	}

	public Object lPop(K key) {
		return getTemplate().opsForList().leftPop(key);
	}

	public void lTrim(K key, long start, long end) {
		getTemplate().opsForList().trim(key, start, end);
	}

	public Object rPop(K key) {
		return getTemplate().opsForList().rightPop(key);
	}

	public List<V> lRange(K key, long start, long end) {
		return getTemplate().opsForList().range(key, start, end);
	}

	public Object lIndex(K key, long index) {
		return getTemplate().opsForList().index(key, index);
	}

	public long lRem(K key, long index, V value) {
		return getTemplate().opsForList().remove(key, index, value).longValue();
	}

	public void lSet(K key, long index, V value) {
		getTemplate().opsForList().set(key, index, value);
	}

	public void lSet(final K key, final List<V> values) {
//		getTemplate().execute(new SessionCallback() {
//			public <K, V> Boolean execute(RedisOperations<K, V> operations)
//					throws DataAccessException {
//				operations.multi();
//				BoundListOperations<K, V> boundListOperations = operations
//						.boundListOps(key);
//				if (!CollectionUtils.isEmpty(values)) {
//					for (int i = 0; i < values.size(); i++) {
//						V v = values.get(i);
//						boundListOperations.rightPush(v);
//					}
//				}
//				operations.exec();
//				return Boolean.valueOf(true);
//			}
//		});
	}

	public long hLen(K key) {
		return getTemplate().opsForHash().size(key).longValue();
	}

	public Map<Object, Object> hGetAll(K key) {
		return getTemplate().opsForHash().entries(key);
	}

	public Object hGet(K key, Object hashKey) {
		return getTemplate().opsForHash().get(key, hashKey);
	}

	public boolean hExists(K key, Object hashKey) {
		return getTemplate().opsForHash().hasKey(key, hashKey).booleanValue();
	}

	public void hSet(K key, Object hashKey, V value) {
		getTemplate().opsForHash().put(key, hashKey, value);
	}

	public void hMSet(K key, Map<K, V> value) {
		getTemplate().opsForHash().putAll(key, value);
	}

	public void hDel(K key, Object hashKey) {
		getTemplate().opsForHash().delete(key, hashKey);
	}

	public void hDel(K key, Object... hashKeys) {
		getTemplate().opsForHash().delete(key, hashKeys);
	}

	public Set<Object> hKeys(K key) {
		return getTemplate().opsForHash().keys(key);
	}

	public List<Object> hVals(K key) {
		return getTemplate().opsForHash().values(key);
	}

	public Set<V> sMembers(K key) {
		return getTemplate().opsForSet().members(key);
	}

	public boolean sIsMember(K key, Object member) {
		return getTemplate().opsForSet().isMember(key, member).booleanValue();
	}

	public Object sPop(K key) {
		return getTemplate().opsForSet().pop(key);
	}

	public boolean sRem(K key, Object member) {
		return getTemplate().opsForSet().remove(key, member).booleanValue();
	}

	public boolean sAdd(K key, V value) {
		return getTemplate().opsForSet().add(key, value).booleanValue();
	}

	public boolean sAdd(final K key, final Set<V> values) {
//		((Boolean) getTemplate().execute(new SessionCallback() {
//			public <K, V> Boolean execute(RedisOperations<K, V> operations)
//					throws DataAccessException {
//				operations.multi();
//				BoundSetOperations<K, V> boundSetOperations = operations
//						.boundSetOps(key);
//				if (!CollectionUtils.isEmpty(values)) {
//					for (Iterator iterator = values.iterator(); iterator
//							.hasNext();) {
//						V v = iterator.next();
//						boundSetOperations.add(v);
//					}
//				}
//				operations.exec();
//				return Boolean.valueOf(true);
//			}
//		})).booleanValue(); TODO
		return true;
	}

	public long sCard(K key) {
		return getTemplate().opsForSet().size(key).longValue();
	}

	public Set<V> zMembers(K key) {
		return getTemplate().opsForZSet().range(key, 0L, zCard(key));
	}

	public Set<V> zRange(K key, long start, long end) {
		return getTemplate().opsForZSet().range(key, start, end);
	}

	public long zCard(K key) {
		return getTemplate().opsForZSet().size(key).longValue();
	}

	public boolean zAdd(K key, V value, double score) {
		return getTemplate().opsForZSet().add(key, value, score).booleanValue();
	}

	public boolean zRem(K key, V value) {
		return getTemplate().opsForZSet().remove(key, value).booleanValue();
	}
}