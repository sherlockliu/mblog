package cn.magicstudio.mblog.analysis;

import java.util.regex.Pattern;

public class CharacterUtil {
	public static Pattern reSkip = Pattern
			.compile("(\\d+\\.\\d+|[a-zA-Z0-9]+)");
	private static final char[] connectors = { '+', '#', '&', '.', '_', '-' };

	public static boolean isChineseLetter(char ch) {
		if ((ch >= '一') && (ch <= 40869))
			return true;
		return false;
	}

	public static boolean isEnglishLetter(char ch) {
		if (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')))
			return true;
		return false;
	}

	public static boolean isDigit(char ch) {
		if ((ch >= '0') && (ch <= '9'))
			return true;
		return false;
	}

	public static boolean isConnector(char ch) {
		char[] arrayOfChar;
		int j = (arrayOfChar = connectors).length;
		for (int i = 0; i < j; i++) {
			char connector = arrayOfChar[i];
			if (ch == connector)
				return true;
		}
		return false;
	}

	public static boolean ccFind(char ch) {
		if (isChineseLetter(ch))
			return true;
		if (isEnglishLetter(ch))
			return true;
		if (isDigit(ch))
			return true;
		if (isConnector(ch))
			return true;
		return false;
	}

	public static char regularize(char input) {
		if (input == '　') {
			return ' ';
		}
		if ((input > 65280) && (input < 65375)) {
			return (char) (input - 65248);
		}
		if ((input >= 'A') && (input <= 'Z')) {
			return input = (char) (input + ' ');
		}
		return input;
	}
}
