package cn.magicstudio.mblog.data.base.mybatis.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public final class UpdateSqlUtils {
	private static final XLogger LOGGER = XLoggerFactory
			.getXLogger(UpdateSqlUtils.class);

	private static final Pattern updatePattern = Pattern.compile("update\\s+",
			2);

	private static final Pattern deletePattern = Pattern.compile("delete\\s+",
			2);

	private static final Pattern wherePattern = Pattern.compile(
			"\\s+where\\s+", 2);

	private static final Pattern oneEqualOnePattern = Pattern.compile(
			"1\\s*=\\s*1", 2);

	private static final Pattern andPattern = Pattern.compile("\\s+and\\s+", 2);

	public static final boolean isSqlSubmittable(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return true;
		}

		if ((updatePattern.matcher(sqlStr).find())
				|| (deletePattern.matcher(sqlStr).find())) {
			if (!wherePattern.matcher(sqlStr).find()) {
				LOGGER.error("SQL should have where clause to prevent from deleting all data:\n"
						+ sqlStr);
				return false;
			}
			if ((oneEqualOnePattern.matcher(sqlStr).find())
					&& (!andPattern.matcher(sqlStr).find())) {
				LOGGER.error("SQL shouldn't have 1=1 clause to prevent from deleting all data:\n"
						+ sqlStr);
				return false;
			}
			return true;
		}

		return true;
	}
}
