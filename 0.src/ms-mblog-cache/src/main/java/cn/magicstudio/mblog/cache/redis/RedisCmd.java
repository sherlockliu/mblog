package cn.magicstudio.mblog.cache.redis;

import cn.wonhigh.retail.backend.core.SpringContext;
import java.util.Collection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

public abstract class RedisCmd {
	private static RedisTemplate<String, String> redisTemplate;

	private static RedisTemplate<String, String> getRedisTemplate() {
		if (redisTemplate == null) {
			redisTemplate = (RedisTemplate) SpringContext
					.getBean("redisTemplate");
		}
		return redisTemplate;
	}

	private static Object execute(ExRedisCallback myRedisCallback) {
		RedisConnectionFactory factory = getRedisTemplate()
				.getConnectionFactory();
		RedisConnection conn = null;
		try {
			conn = RedisConnectionUtils.getConnection(factory);
			boolean pipelineStatus = conn.isPipelined();
			if (!pipelineStatus) {
				conn.openPipeline();
			}
			ExJedis myJedis = new ExJedis((Jedis) conn.getNativeConnection());
			Object obj = myRedisCallback.doWithMyJedis(myJedis);

			if (!pipelineStatus) {
				conn.closePipeline();
			}
			return obj;
		} finally {
			RedisConnectionUtils.releaseConnection(conn, factory);
		}
	}

	public static Collection<String> zrangebylex(final String key,
			final String min, String max, final int offset, final int take) {
		// (Collection)execute(new ExRedisCallback() {
		// public Object doWithMyJedis(ExJedis myJedis) {
		// String maxValue = "+";
		// if (RedisCmd.this != null) {
		// maxValue = RedisCmd.this;
		// }
		// String minValue = "-";
		// if (minValue != null) {
		// minValue = "[" + min;
		// }
		// return myJedis.zrangebylex(key, minValue, maxValue, offset, take);
		// } TODO
		return null;
		// });
	}
}
