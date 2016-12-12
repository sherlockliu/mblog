package cn.magicstudio.mblog.base.framework.utils;

import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReflectHelper {
	private static final Log log = LogFactory.getLog(ReflectHelper.class);

	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				log.error(e);
			}
		}
		return null;
	}

	public static Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	public static void setValueByFieldName(Object obj, String fieldName,
			Object value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}

	public static Object createClassObject(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
			if (clazz != null) {
				return clazz.newInstance();
			}
		} catch (InstantiationException e) {
			log.error(e);
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (ClassNotFoundException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
