package cn.magicstudio.mblog.data.core;

import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "update", args = { java.sql.Statement.class }) })
public class UpdateInterceptor implements Interceptor {
	protected static final XLogger logger = XLoggerFactory
			.getXLogger(UpdateInterceptor.class);

	private static final String SHARDPARAM = "shardingflag";
	private static final String BOUNDSQL_NAME = "delegate.boundSql";
	private static final String BOUNDSQL_SQL_NAME = "delegate.boundSql.sql";
	private static final String SQL_PARAM_NAME = "delegate.parameterHandler.parameterObject";
	boolean registed;

	public Object intercept(Invocation invocation) throws Throwable {
		String statement = null;
		try {
			MetaObject metaObject = DbHelper.getMappedStatement(invocation);
			BoundSql boundSql = (BoundSql) metaObject
					.getValue("delegate.boundSql");
			Configuration config = (Configuration) metaObject
					.getValue("delegate.parameterHandler.configuration");

			Object values = metaObject
					.getValue("delegate.parameterHandler.parameterObject");

			MetaObject params = null;
			if (values != null) {
				params = config.newMetaObject(values);
			}
		} catch (Exception e) {
			logger.warn("sharding format error", e);
		}
		return invocation.proceed();
	}

	private void trigger() {
	}

	public Object plugin(Object target) {
		if ((target instanceof StatementHandler)) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	public void setProperties(Properties properties) {
	}
}
