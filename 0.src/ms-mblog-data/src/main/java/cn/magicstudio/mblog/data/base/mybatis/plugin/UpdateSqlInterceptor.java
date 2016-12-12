package cn.magicstudio.mblog.data.base.mybatis.plugin;

import java.util.Properties;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = {
		MappedStatement.class, Object.class }) })
public class UpdateSqlInterceptor implements Interceptor {
	private static final XLogger LOGGER = XLoggerFactory
			.getXLogger(UpdateSqlInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {
		String sqlStr = "";
		try {
			Object[] updateArgs = invocation.getArgs();
			MappedStatement statement = (MappedStatement) updateArgs[0];

			BoundSql sql = statement.getSqlSource().getBoundSql(null);
			sqlStr = sql.getSql();
		} catch (Exception e) {
			LOGGER.error(
					"Exception in update SQL interceptor during retrieving SQL:",
					e);
		}
		boolean isValid = UpdateSqlUtils.isSqlSubmittable(sqlStr);
		if (isValid) {
			return invocation.proceed();
		}
		throw new IllegalStateException(
				"Invalid update SQL in Wonhigh DML semantic:" + sqlStr);
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}
}
