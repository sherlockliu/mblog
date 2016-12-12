package cn.magicstudio.mblog.base.framework.utils;

import com.yougou.logistics.base.common.enums.CacheExpireEnum;
import com.yougou.logistics.base.common.enums.CacheStateEnum;
import com.yougou.logistics.base.common.enums.CacheTypeEnum;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;

public class CacheManage implements CacheManageMBean {
	private static final String CACHE_JMX_ENABLE = "cache.jmx.enable";
	private static final Logger log = Logger.getLogger(CacheManage.class
			.getName());

	public static final String MBEAN_CACHE_MANAGER = "com.yougou.logistics.base.common.utils:type=CacheManage";

	private static MBeanServer mbs;

	private MBeanInfo mBeanInfo;

	private static Map<CacheTypeEnum, LocalCache> caches = new HashMap();
	
	static {
		for (CacheTypeEnum o : CacheTypeEnum.values()) {
			if (CacheStateEnum.STATIC == o.getState()) {
				caches.put(o, new StaticCache(1024));
			} else if (CacheStateEnum.LRU == o.getState()) {
				caches.put(o, new LRUCache(getCacheMaxSizeProperties(o), 1024));
			}
		}

		String jmxEnable = System.getProperty("cache.jmx.enable");
		if (new Boolean(jmxEnable).booleanValue()) {
			registerUnregisterJMX(true);
		}
	}

	@Deprecated
	public static boolean containsKey(String key, CacheTypeEnum type) {
		return getLocalCache(type).valideKey(key);
	}

	public static void put(String key, Object value, CacheTypeEnum type) {
		put(key, value, type, type.getExpire());
	}

	public static void put(String key, Object value, CacheTypeEnum type,
			CacheExpireEnum expire) {
		getLocalCache(type).put(key, value, expire.getExpire());
	}

	private static final int DEFAULE_LRU_MAX_SIZE = 10240;

	private static Thread cleanThread;

	public static Object get(String key, CacheTypeEnum type) {
		return getLocalCache(type).get(key);
	}

	public static Object getAndProLong(String key, CacheTypeEnum type) {
		return getLocalCache(type).getAndProlong(key,
				type.getExpire().getExpire());
	}

	public static Map<String, Object> getAll(List<String> keys,
			CacheTypeEnum type) {
		if (null == keys)
			return null;
		Map<String, Object> ret = new HashMap();
		for (String key : keys) {
			ret.put(key, get(key, type));
		}
		return ret;
	}

	public static void putAll(Map<String, ?> map, CacheTypeEnum type) {
		if (null == map)
			return;
		int i = 0;
		for (Map.Entry<String, ?> o : map.entrySet()) {
			put((String) o.getKey(), o.getValue(), type);
			i++;
			if (i % 1000 == 0) {
				try {
					Thread.sleep(0L);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.log(Level.SEVERE,
							"CacheManage put all data field fail！", e);
					return;
				}
			}
		}
	}

	public static void clear(CacheTypeEnum type) {
		getLocalCache(type).clear();
	}

	public void clearAll()
   {
     for (CacheTypeEnum type : CacheTypeEnum.values()) {
       clear(type);
     }
   }

	public static void remove(String key, CacheTypeEnum type) {
		getLocalCache(type).remove(key);
	}

	public static int size(CacheTypeEnum type) {
		if (null == type)
			throw new NullPointerException("type is null");
		return getLocalCache(type).size();
	}

	public static CumulativeStats getStats(CacheTypeEnum type) {
		return getLocalCache(type).getStats();
	}

	public static void startCleanExpiredThread() {
		if (null == cleanThread)
			cleanThread = new Thread(new ResetValue(null));
		cleanThread.start();
	}

	public static void shutdownCleanExpiredThread() {
		if (null != cleanThread)
			cleanThread.interrupt();
		ResetValue.setStopFlagTrue();
	}

	private static class ResetValue implements Runnable {
		private static volatile boolean RUN_FLAG;

		private static volatile boolean LOOP_FLAG;
		private static volatile boolean STOP_FLAG;
		public ResetValue(Object o){
			
		}

		public void run() {
			if (RUN_FLAG)
				return;
			RUN_FLAG = true;
			long diff = CacheManage.calcNextDayTimeInMillis(1)
					- System.currentTimeMillis();
			do {
				try {
					Thread.sleep(diff);
				} catch (InterruptedException e) {
					CacheManage.log.log(Level.WARNING,
							"CacheManage clean expired thread stop!");
					break;
				}
				if (!LOOP_FLAG) {
					LOOP_FLAG = true;
					for (CacheTypeEnum type : CacheTypeEnum.values())
						if (CacheStateEnum.STATIC == type.getState()) {
							CacheManage.getLocalCache(type).clearIfExpired();
							try {
								Thread.sleep(0L);
							} catch (InterruptedException e) {
								CacheManage.log
										.log(Level.WARNING,
												"CacheManage clean expired thread stop!");
								break;
							}
						}
					LOOP_FLAG = false;
					diff = CacheManage.calcNextDayTimeInMillis(1)
							- System.currentTimeMillis();
				}
			} while (!STOP_FLAG);
			RUN_FLAG = false;
			STOP_FLAG = false;
		}

		public static void setStopFlagTrue() {
			STOP_FLAG = true;
		}
	}

	private static long calcNextDayTimeInMillis(int hourInDay) {
		Calendar c = Calendar.getInstance();
		c.set(14, 0);
		c.set(13, 0);
		c.set(12, 0);
		c.set(11, hourInDay);
		c.add(5, 1);
		return c.getTimeInMillis();
	}

	private static LocalCache getLocalCache(CacheTypeEnum type) {
		return (LocalCache) caches.get(type);
	}

	private static int getCacheMaxSizeProperties(CacheTypeEnum type) {
		if (null == type) {
			return 10240;
		}
		String value = null;
		if ((null == value) || (value.length() == 0))
			return 10240;
		int i = 0;
		for (int len = value.length(); i < len; i++) {
			if (!Character.isDigit(value.charAt(i)))
				return 10240;
		}
		try {
			return Integer.valueOf(value).intValue();
		} catch (Exception localException) {
		}
		return 10240;
	}

	public static void registerUnregisterJMX(boolean doRegister) {
//		if (mbs == null) {
//			mbs = ManagementFactory.getPlatformMBeanServer();
//		}
//		try {
//			ObjectName name = new ObjectName(
//					"com.yougou.logistics.base.common.utils:type=CacheManage");
//
//			if (doRegister) {
//				if (!mbs.isRegistered(name)) {
//					mbs.registerMBean(new CacheManage(), name);
//				}
//			} else if (mbs.isRegistered(name)) {
//				mbs.unregisterMBean(name);
//			}
//		} catch (Exception e) {
//			log.log(Level.CONFIG, "Unable to start/stop JMX", e);
//		} TODO
	}
}
