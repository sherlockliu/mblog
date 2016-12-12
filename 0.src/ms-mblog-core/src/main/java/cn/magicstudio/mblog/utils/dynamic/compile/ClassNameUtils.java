package cn.magicstudio.mblog.utils.dynamic.compile;

public class ClassNameUtils {
	public static String convertClassNameToUnderscoreName(String name) {
		StringBuilder result = new StringBuilder();

		if (name != null) {
			int len = name.length();

			if (len > 0) {
				result.append(name.charAt(0));

				for (int i = 1; i < len; i++) {
					if (Character.isUpperCase(name.charAt(i))) {
						result.append('_');
					}

					result.append(name.charAt(i));
				}
			}
		}

		return result.toString().toUpperCase();
	}

	public static String convertUnderscoreNameToClassName(String name) {
		StringBuffer result = new StringBuffer();
		boolean nextIsUpper = false;

		if (name != null) {
			int len = name.length();

			if (len > 0) {
				String s = String.valueOf(name.charAt(0));

				result.append(s.toUpperCase());

				for (int i = 1; i < len; i++) {
					s = String.valueOf(name.charAt(i));

					if ("_".equals(s)) {
						nextIsUpper = true;
					} else if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					} else {
						result.append(s.toLowerCase());
					}
				}
			}
		}

		return result.toString();
	}
}