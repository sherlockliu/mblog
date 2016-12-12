package cn.magicstudio.mblog.base.framework.uuid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Resource {
	private static final Logger LOG = LoggerFactory.getLogger(Resource.class);

	private static Map<String, String> insteadOfClose = new ConcurrentHashMap(13);

	private static Map<String, String> beforeClose = new ConcurrentHashMap(5);

	private static Map<String, String> afterClose = new ConcurrentHashMap(5);

	static {
		beforeClose("javax.jms.Connection", "stop");
		beforeClose("javax.imageio.ImageWriter", "reset");
		beforeClose("javax.imageio.stream.ImageInputStream", "flush");

		insteadOfClose("com.eaio.nativecall.NativeCall", "destroy");
		insteadOfClose("com.jcraft.jsch.Channel", "disconnect");
		insteadOfClose("de.intarsys.cwt.environment.IGraphicsContext",
				"dispose");
		insteadOfClose("groovyx.net.http.HTTPBuilder", "shutdown");
		insteadOfClose("java.lang.Process", "destroy");
		insteadOfClose("javax.imageio.ImageReader", "dispose");
		insteadOfClose("javax.imageio.ImageWriter", "dispose");
		insteadOfClose("org.apache.http.impl.client.AbstractHttpClient",
				"shutdown");
		insteadOfClose("org.infinispan.Cache", "stop");
		insteadOfClose("org.infinispan.manager.DefaultCacheManager", "stop");
	}

	public static void beforeClose(Class<?> clazz, String method) {
		beforeClose(clazz.getName(), method);
	}

	public static void beforeClose(String className, String method) {
		beforeClose.put(className, method);
	}

	public static void afterClose(Class<?> clazz, String method) {
		afterClose(clazz.getName(), method);
	}

	public static void afterClose(String className, String method) {
		afterClose.put(className, method);
	}

	public static void insteadOfClose(Class<?> clazz, String method) {
		insteadOfClose(clazz.getName(), method);
	}

	public static void insteadOfClose(String className, String method) {
		insteadOfClose.put(className, method);
	}

	public static void close(Object... objects) {
		if (objects == null) {
			return;
		}
		for (Object object : objects) {
			if (object != null) {
				callFromMap(beforeClose, object);
				if (!callFromMap(insteadOfClose, object)) {
					callVoidMethod(object, "close");
				}
				callFromMap(afterClose, object);
			}
		}
	}

	private static boolean callFromMap(Map<String, String> map, Object object) {
		return callFromMap(map, object, object.getClass());
	}

	private static boolean callFromMap(Map<String, String> map, Object object,
			Class<?> currClass) {
		String currentClassName = currClass.getName();

		String voidMethod = map == null ? null : (String) map
				.get(currentClassName);
		if (voidMethod != null) {
			callVoidMethod(object, voidMethod);
			return true;
		}

		if (callFromMapFromInterfaces(map, object, currClass)) {
			return true;
		}

		if ((hasSuperclass(currClass))
				&& (callFromMap(map, object, currClass.getSuperclass()))) {
			return true;
		}

		return false;
	}

	private static boolean callFromMapFromInterfaces(Map<String, String> map,
			Object object, Class<?> currClass) {
		boolean atLeastOneMethodCalled = false;
		for (Class<?> currentInterface : currClass.getInterfaces()) {
			if (callFromMap(map, object, currentInterface)) {
				atLeastOneMethodCalled = true;
			}
		}
		return atLeastOneMethodCalled;
	}

	private static boolean hasSuperclass(Class<?> clazz) {
		return (clazz != null) && (clazz != Object.class)
				&& (clazz.getSuperclass() != null);
	}

	private static void callVoidMethod(Object object, String method) {
		try {
			Method m = object.getClass().getMethod(method, new Class[0]);
			m.invoke(object, new Object[0]);
		} catch (NoSuchMethodException ex) {
			log(object, ex);
		} catch (IllegalArgumentException ex) {
			log(object, ex);
		} catch (IllegalAccessException ex) {
			log(object, ex);
		} catch (InvocationTargetException ex) {
			log(object, ex.getCause());
		} catch (SecurityException ex) {
			log(object, ex);
		}
	}

	private static void log(Object object, Throwable throwable) {
		if (LOG.isTraceEnabled()) {
			LOG.warn(object.getClass().getName(), throwable);
		} else if (LOG.isDebugEnabled()) {
			LOG.warn(object.getClass().getName() + ": "
					+ throwable.getClass().getName() + ": "
					+ throwable.getLocalizedMessage());
		}
	}
}
