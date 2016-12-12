package cn.magicstudio.mblog.data.base.mybatis.plugin;

import com.yougou.logistics.base.common.enums.log.JsonType;
import com.yougou.logistics.base.common.enums.log.LogType;
import com.yougou.logistics.base.common.enums.log.SystemCode;
import com.yougou.logistics.base.common.model.LogBase;
import com.yougou.logistics.base.common.model.SystemUser;
import com.yougou.logistics.base.common.utils.ReflectHelper;
import com.yougou.logistics.base.common.utils.ThreadLocalSystemUserVar;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.statement.StatementHandler.class, method = "prepare", args = { java.sql.Connection.class }) })
public class LogPlugin implements Interceptor {
	private String includeTables;
	private String excludeTables;
	private String selectTables;
	private Object obj = new Object();

	private Map<String, Object> includeTablesMap = new ConcurrentHashMap();

	private boolean include;

	private Map<String, Object> excludeTablesMap = new ConcurrentHashMap();

	private String dmls = "update,delete";

	private SystemCode systemCode;

	private static final XLogger LOGGER = XLoggerFactory
			.getXLogger(LogPlugin.class);

	private Pattern p = Pattern
			.compile("(?i)(?<=(?:from|into|update|join)\\s{1,})(\\w+)");

	public Object intercept(Invocation ivk) throws Throwable {
		if (((ivk.getTarget() instanceof RoutingStatementHandler))
				&& (this.systemCode != null)) {
			try {
				RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk
						.getTarget();
				BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper
						.getValueByFieldName(statementHandler, "delegate");

				MappedStatement mappedStatement = (MappedStatement) ReflectHelper
						.getValueByFieldName(delegate, "mappedStatement");

				if (((this.selectTables != null) && (!this.selectTables
						.equals("")))
						|| (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT)) {
					BoundSql boundSql = delegate.getBoundSql();
					String tableName = getTableName(boundSql.getSql(),
							mappedStatement.getSqlCommandType());
					if ((tableName != null)
							&& (!this.excludeTablesMap.containsKey(tableName))
							&& ((!this.include) || ((this.include) && (this.includeTablesMap
									.containsKey(tableName))))) {
						getLog(boundSql, mappedStatement, tableName);
					}
				}
			} catch (Exception e) {
				LOGGER.error("LogPlugin Exception ", e);
			}
		}
		return ivk.proceed();
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		this.includeTables = p.getProperty("includeTables");
		if ((this.includeTables != null) && (!this.includeTables.equals(""))) {
			String[] tabKeys = this.includeTables.split(",");
			if ((tabKeys != null) && (tabKeys.length > 0)) {
				for (String string : tabKeys) {
					this.includeTablesMap.put(string, this.obj);
				}
				this.include = true;
			}
		}
		this.excludeTables = p.getProperty("excludeTables");
		if ((this.excludeTables != null) && (!this.excludeTables.equals(""))) {
			String[] tabKeys = this.excludeTables.split(",");
			if ((tabKeys != null) && (tabKeys.length > 0)) {
				for (String string : tabKeys) {
					this.excludeTablesMap.put(string, this.obj);
				}
			}
		}
		String syscode = p.getProperty("syscode");
		for (SystemCode code : SystemCode.values()) {
			if (code.name().toString().equalsIgnoreCase(syscode)) {
				this.systemCode = code;
				break;
			}
		}
		String dmls = p.getProperty("dmls");
		if ((dmls != null) && (!dmls.equals(""))) {
			this.dmls = dmls;
		}
		this.selectTables = p.getProperty("selectTables");
	}

	private String getTableName(String sql, SqlCommandType type) {
		if (this.dmls.indexOf(type.name().toLowerCase()) < 0)
			return null;
		StringBuffer sb = new StringBuffer();
		Matcher m = this.p.matcher(sql);
		while (m.find()) {
			sb.append(m.group()).append(",");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private LogType getLogType(SqlCommandType type) {
    for (LogType logType : ) {
      if (logType.name().equals(type.name()))
        return logType;
    }
    return null;
  }

	private LogBase getLog(BoundSql boundSql, MappedStatement mappedStatement,
			String tablesName) {
		ObjectMapper objectMapper = new ObjectMapper();
		String userMapJson = null;
		try {
			userMapJson = objectMapper.writeValueAsString(boundSql
					.getParameterObject());
		} catch (JsonGenerationException e) {
			LOGGER.error("LogPlugin parse error ...", e);
		} catch (JsonMappingException e) {
			LOGGER.error("LogPlugin parse error ...", e);
		} catch (IOException e) {
			LOGGER.error("LogPlugin parse error ...", e);
		}
		LogBase log = LogBase.getLog(this.systemCode, -1);
		log.setLogType(getLogType(mappedStatement.getSqlCommandType()));
		log.bulidTargetObject(0, "", userMapJson);
		SystemUser systemUser = ThreadLocalSystemUserVar.get();
		if (systemUser != null) {
			log.bulidOperInfo(systemUser.getUserid().intValue(),
					systemUser.getLoginName(), systemUser.getIp());
		} else {
			LOGGER.warn("LogPlugin MISS OPERINFO....PLS,CHECK...");
		}
		log.setTablesName(tablesName);
		log.setJsonType(JsonType.AUTO);

		return log;
	}
}