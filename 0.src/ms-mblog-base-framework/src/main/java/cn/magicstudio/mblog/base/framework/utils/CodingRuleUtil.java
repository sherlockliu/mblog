package cn.magicstudio.mblog.base.framework.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CodingRuleUtil {
	public static String initFormatedDate(int resetMode) {
		DateFormat df = new SimpleDateFormat(getFormatPattern(resetMode));
		return df.format(new Date());
	}

	public static String getFormatedDate(int resetMode, Date date) {
		if (date == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(getFormatPattern(resetMode));
		return df.format(date);
	}

	public static String getFormatPattern(int resetMode) {
		String pattern = "";
		switch (resetMode) {
		case 1:
			pattern = "yyyyMMdd";
			break;
		case 2:
			pattern = "yyyyMM";
			break;
		case 3:
			pattern = "yyyy";
		}

		return pattern;
	}

	public static void main(String[] args) {
		int resetMode = 1;
		Date now = new Date();

		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(1), cal.get(2), cal.get(5), 23, 0, 0);
		Date date1 = cal.getTime();
		String resetTime = getFormatedDate(resetMode, date1);

		String dbTime = getFormatedDate(resetMode, now);
		System.out.println("resetTime:" + resetTime);
		System.out.println("dbTime:" + dbTime);

		if (("".equals(resetTime)) || (!dbTime.equals(resetTime))) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}
	}
}
