package cn.magicstudio.mblog.base.framework.utils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class BeanUtilsCommon {
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String[] TYPE_SIMPLE = { "java.lang.Integer", "int",
			"java.util.Date" };
	public static String TYPE_INTEGER = "java.lang.Integer,int";
	public static String TYPE_DATE = "java.util.Date";

	public static String splitSpace(String str) throws ParseException {
		if (str.contains(" ")) {
			return str.split(" ")[1];
		}
		return str;
	}

	public static boolean isSimpleType(String type) {
		for (int i = 0; i < TYPE_SIMPLE.length; i++) {
			if (type.equals(TYPE_SIMPLE[i])) {
				return true;
			}
		}
		return false;
	}

	public static Integer parseInteger(String str) {
		if ((str == null) || (str.equals(""))) {
			return Integer.valueOf(0);
		}
		return Integer.valueOf(Integer.parseInt(str));
	}

	public static Date parseDate(String str) throws ParseException {
		if ((str == null) || (str.equals(""))) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date date = sdf.parse(str);
		return date;
	}

	public static Object parseObject(Class clazz, String str)
			throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		Object obj;
		
		if ((str == null) || (str.equals(""))) {
			obj = null;
		} else {
			obj = clazz.newInstance();
			Method m = clazz.getMethod("setId", new Class[] { str.getClass() });
			m.invoke(obj, new Object[] { str });
		}
		return obj;
	}

	public static Object parseByType(Class clazz, String str)
			throws ParseException, InstantiationException,
			IllegalAccessException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InvocationTargetException {
		Object r = "";
		String clazzName = splitSpace(clazz.getName());
		if (isSimpleType(clazzName)) {
			if (TYPE_INTEGER.contains(clazzName)) {
				r = parseInteger(str);
			} else if (TYPE_DATE.contains(clazzName)) {
				r = parseDate(str);
			}
		} else {
			r = parseObject(clazz, str);
		}
		return r;
	}

	public static void copyProperties(Map map, Object obj) throws Exception {
		BeanInfo targetbean = java.beans.Introspector.getBeanInfo(obj
				.getClass());
		PropertyDescriptor[] propertyDescriptors = targetbean
				.getPropertyDescriptors();

		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor pro = propertyDescriptors[i];
			Method wm = pro.getWriteMethod();
			if (wm != null) {
				Iterator ite = map.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();

					if (key.toLowerCase().equals(pro.getName().toLowerCase())) {
						if (!Modifier.isPublic(wm.getDeclaringClass()
								.getModifiers())) {
							wm.setAccessible(true);
						}
						Object value = ((String[]) (String[]) map.get(key))[0];
						String pt = splitSpace(pro.getPropertyType().getName());

						if (!pt.equals(value.getClass().getName())) {
							value = parseByType(pro.getPropertyType(),
									value.toString());
						}

						wm.invoke(obj, new Object[] { value });
						break;
					}
				}
			}
		}
	}

	public static void object2MapWithoutNull(Object obj, Map map)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);

			if (fields[j].get(obj) != null) {
				if ((fields[j].get(obj) instanceof Date)) {
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
					map.put(fields[j].getName(), sdf.format(fields[j].get(obj)));
				} else {
					map.put(fields[j].getName(), fields[j].get(obj));
				}
			} else {
				map.put(fields[j].getName(), "");
			}
		}

		Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();
		for (int j = 0; j < fields2.length; j++) {
			fields2[j].setAccessible(true);

			if (fields2[j].get(obj) != null) {
				if ((fields2[j].get(obj) instanceof Date)) {
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
					map.put(fields2[j].getName(),
							sdf.format(fields2[j].get(obj)));
				} else {
					map.put(fields2[j].getName(), fields2[j].get(obj));
				}
			} else {
				map.put(fields2[j].getName(), "");
			}
		}
	}

	public static boolean validateDate(String value) {
		if ((value == null) || (value.equals("")))
			return false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setLenient(false);
			sdf.parse(value);
			System.out.println(sdf.parse(value));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static Object setFieldValue(Map<String, String> map, Class<?> cls)
			throws Exception {
		Field[] fields = cls.getDeclaredFields();

		Object obj = cls.newInstance();
		for (Field field : fields) {
			Class<?> clsType = field.getType();
			String name = field.getName();
			String strSet = "set" + name.substring(0, 1).toUpperCase()
					+ name.substring(1, name.length());
			Method methodSet = cls.getDeclaredMethod(strSet,
					new Class[] { clsType });
			if (map.containsKey(name)) {
				Object objValue = typeConversion(clsType,
						(String) map.get(name));
				methodSet.invoke(obj, new Object[] { objValue });
			}
		}
		return obj;
	}

	public static Object typeConversion(Class<?> cls, String str) {
		Object obj = null;
		String nameType = cls.getSimpleName();
		if ("Integer".equals(nameType)) {
			obj = Integer.valueOf(str);
		}
		if ("String".equals(nameType)) {
			obj = str;
		}
		if ("Float".equals(nameType)) {
			obj = Float.valueOf(str);
		}
		if ("Double".equals(nameType)) {
			obj = Double.valueOf(str);
		}

		if ("Boolean".equals(nameType)) {
			obj = Boolean.valueOf(str);
		}
		if ("Long".equals(nameType)) {
			obj = Long.valueOf(str);
		}

		if ("Short".equals(nameType)) {
			obj = Short.valueOf(str);
		}

		if ("Character".equals(nameType)) {
			obj = Character.valueOf(str.charAt(1));
		}

		return obj;
	}
}
