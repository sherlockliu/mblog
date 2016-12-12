package cn.magicstudio.mblog.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
	public static String templatePath;

	public Helper() {
		templatePath = System.getProperty("java.io.tmpdir");
	}

	private static boolean isMatch(String regex, String orginal) {
		if ((orginal == null) || (orginal.trim().equals(""))) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	public static boolean isInteger(String orginal) {
		return isMatch("^-?\\d+$", orginal);
	}

	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	public static boolean isWholeNumber(String orginal) {
		return (isMatch("[+-]{0,1}0", orginal)) || (isPositiveInteger(orginal))
				|| (isNegativeInteger(orginal));
	}

	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	public static boolean isRealNumber(String orginal) {
		return (isWholeNumber(orginal)) || (isDecimal(orginal));
	}

	public static String combine(String... strings) {
		if ((strings == null) || (strings.length == 0))
			throw new NullPointerException("strings");
		String path = strings[0];
		if (strings.length == 1) {
			return path;
		}
		for (int i = 1; i < strings.length; i++) {
			String str = strings[i];
			if (strings[i].endsWith(File.separator)) {
				str = str.substring(0, str.length() - 2);
			}
			if (strings[i].startsWith(File.separator)) {
				str = str.substring(1, str.length() - 1);
			}
			path = path + File.separator + str;
		}

		return path;
	}

	public static String readFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		int fileLen = (int) file.length();
		char[] chars = new char[fileLen];
		try {
			reader.read(chars);
			String txt = String.valueOf(chars);
			return txt;
		} finally {
			reader.close();
		}
	}

	public static String readFile(String path) throws IOException {
		File file = new File(path);
		return readFile(file);
	}

	public static void createTemplateFolder(String destDirName) {
		destDirName = System.getProperty("java.io.tmpdir") + File.separator
				+ destDirName;
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File dir = new File(destDirName);
		if (dir.exists()) {
			return;
		}

		dir.mkdirs();
	}

	public static String getResourcesAsString(String resourceLocation)
			throws FileNotFoundException {
		File file = org.springframework.util.ResourceUtils
				.getFile(resourceLocation);
		return readFileAsText(file);
	}

	public static String readFileAsText(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String readString(InputStream stream) throws IOException {
		StringBuilder result = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String s = null;
		while ((s = br.readLine()) != null) {
			result.append(s + System.lineSeparator());
		}
		br.close();

		return result.toString();
	}

	public static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] chunk = new byte['Ѐ'];
		int count;
		while ((count = in.read(chunk)) >= 0) {
			out.write(chunk, 0, count);
		}
	}
}