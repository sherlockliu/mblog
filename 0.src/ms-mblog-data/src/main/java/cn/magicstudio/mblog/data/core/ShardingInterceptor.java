package cn.magicstudio.mblog.data.core;
 
 import cn.wonhigh.retail.backend.core.ApplicationContext;
 import cn.wonhigh.retail.backend.monitor.PerformanceCounter;
 import cn.wonhigh.retail.backend.monitor.PerformanceLogger;
 import cn.wonhigh.retail.backend.monitor.PerformanceManager;
 import java.lang.reflect.Method;
 import java.util.List;
 import java.util.Properties;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import javax.annotation.Resource;
 import org.apache.commons.lang.StringUtils;
 import org.apache.ibatis.executor.statement.StatementHandler;
 import org.apache.ibatis.mapping.BoundSql;
 import org.apache.ibatis.mapping.MappedStatement;
 import org.apache.ibatis.mapping.ParameterMapping;
 import org.apache.ibatis.plugin.Interceptor;
 import org.apache.ibatis.plugin.Intercepts;
 import org.apache.ibatis.plugin.Invocation;
 import org.apache.ibatis.plugin.Plugin;
 import org.apache.ibatis.reflection.MetaObject;
 import org.apache.ibatis.session.Configuration;
 import org.slf4j.ext.XLogger;
 import org.slf4j.ext.XLoggerFactory;
 
 @Intercepts({@org.apache.ibatis.plugin.Signature(type=StatementHandler.class, method="query", args={java.sql.Statement.class, org.apache.ibatis.session.ResultHandler.class}), @org.apache.ibatis.plugin.Signature(type=StatementHandler.class, method="update", args={java.sql.Statement.class}), @org.apache.ibatis.plugin.Signature(type=StatementHandler.class, method="prepare", args={java.sql.Connection.class})})
 public class ShardingInterceptor
   implements Interceptor
 {
   protected static final XLogger logger = XLoggerFactory.getXLogger(ShardingInterceptor.class);
   private PerformanceCounter counter;
   private PerformanceCounter ecounter;
   @Resource
   DbHelper helper;
   
   private PerformanceCounter getCounter() { if (this.counter == null)
       this.counter = PerformanceManager.getInstance().getCounter("db.query");
     return this.counter;
   }
   
 
   private PerformanceCounter getErrorCounter()
   {
     if (this.ecounter == null) {
       this.ecounter = PerformanceManager.getInstance().getCounter("db.query.error");
     }
     return this.ecounter;
   }
   
 
   private static final String SHARDPARAM = "shardingflag";
   
   private static final String BOUNDSQL_NAME = "delegate.boundSql";
   
   private static final String BOUNDSQL_SQL_NAME = "delegate.boundSql.sql";
   
   private static final String SQL_PARAM_NAME = "delegate.parameterHandler.parameterObject";
   
   private String FLAG = "/*#mycat:%s=select 1 from %s where sharding_flag='%s' */";
   private String READ_FLAG = "balance";
   private String WRITE_FLAG = "sql";
   
 
   private String table = "bill_loss";
   
 
   private String getShardingTag(String tableName, Object val, boolean balance)
   {
     String flag = "";
     if (val != null) {
       flag = val.toString().replaceAll("'", "");
     }
     return String.format(this.FLAG, new Object[] { balance ? this.READ_FLAG : this.WRITE_FLAG, this.table, flag });
   }
   
   private Object findShardingValue(MetaObject metaObject, BoundSql boundSql)
   {
     try {
       List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
       
       Object value = null;
       for (int i = 0; i < parameterMappings.size(); i++) {
         ParameterMapping parameterMapping = (ParameterMapping)parameterMappings.get(i);
         String propertyName = parameterMapping.getProperty();
         if ((propertyName != null) && (StringUtils.indexOfIgnoreCase(propertyName, "shardingflag") >= 0))
         {
 
           if (boundSql.hasAdditionalParameter(propertyName)) {
             value = boundSql.getAdditionalParameter(propertyName);
           } else if (metaObject == null) {}
           return metaObject.getValue(propertyName);
         }
       }
     }
     catch (Exception e) {
       logger.warn("sharding format error", e);
     }
     return null;
   }
   
   final Pattern regex = Pattern.compile("('U\\d+_\\w')|('0_Z')", 2);
   
   final Pattern balanceRegex = Pattern.compile("/\\*balance\\*/", 2);
   static final String local_count_key = "__db_counter";
   
   String prepareUpdateStatement(MetaObject metaObject, BoundSql boundSql, String script)
   {
     if (script.startsWith("/*#mycat")) {
       return null;
     }
     String tableName = null;
     Matcher m = this.regex.matcher(script);
     
     boolean balance = this.balanceRegex.matcher(script).find();
     if (balance) {
       script = script.replace("/*balance*/", "");
     }
     if (m.find()) {
       return getShardingTag(tableName, m.group(0), balance) + script;
     }
     Object val = findShardingValue(metaObject, boundSql);
     
     if (val != null) {
       return getShardingTag(tableName, val, balance) + script;
     }
     return null;
   }
   
   public Object intercept(Invocation invocation) throws Throwable
   {
     long time = System.currentTimeMillis();
     MetaObject metaObject = DbHelper.getMappedStatement(invocation);
     MappedStatement mapped = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
     
     String url = mapped.getId();
     
     String method = invocation.getMethod().getName();
     String type = "db-";
     type = type + ("update".equals(method) ? "u" : "q");
     
     Integer total = null;
     if (("query".equals(method)) || ("update".equals(method))) {
       total = (Integer)ApplicationContext.current().getValue("__db_counter");
       if (total == null) {
         total = Integer.valueOf(1);
       } else
         total = Integer.valueOf(total.intValue() + 1);
       ApplicationContext.current().setValue("__db_counter", total);
     }
     try
     {
       if ((this.enable) && ("prepare".equals(method))) {
         prepareStatement(metaObject);
       }
       Object result = invocation.proceed();
       if (("query".equals(method)) || ("update".equals(method))) {
         long take = System.currentTimeMillis() - time;
         String tag = total + "";
         if (((result instanceof List)) && (result != null)) {
           tag = tag + ":" + ((List)result).size();
         }
         PerformanceLogger.log(type, url, "ok", take, tag);
         getCounter().increment(take);
       }
       
       return result;
     } catch (Exception e) {
       if (("query".equals(method)) || ("update".equals(method))) {
         long take = System.currentTimeMillis() - time;
         
         PerformanceLogger.log(type, url, "error", take, total + "");
         getErrorCounter().increment(take);
       }
       throw e;
     }
   }
   
 
   private boolean prepareStatement(MetaObject metaObject)
   {
     String statement = null;
     
     BoundSql boundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
     Configuration config = (Configuration)metaObject.getValue("delegate.parameterHandler.configuration");
     
     Object values = metaObject.getValue("delegate.parameterHandler.parameterObject");
     
     MetaObject params = null;
     if (values != null)
       params = config.newMetaObject(values);
     String script = boundSql.getSql();
     ApplicationContext.current().setValue("excute-is-query", Boolean.valueOf(true));
     if ((StringUtils.indexOfIgnoreCase(script, "INSERT INTO") >= 0) || (StringUtils.indexOfIgnoreCase(script, "UPDATE ") >= 0) || (null == script))
     {
 
       return true;
     }
     statement = prepareUpdateStatement(params, boundSql, script);
     if (null != statement)
       metaObject.setValue("delegate.boundSql.sql", statement);
     return false;
   }
   
   public Object plugin(Object target)
   {
     if ((target instanceof StatementHandler)) {
       return Plugin.wrap(target, this);
     }
     return target;
   }
   
 
   private boolean enable = true;
   private boolean triger = false;
   
 
   public void setProperties(Properties properties)
   {
     if (properties.containsKey("rule")) {
       this.FLAG = ((String)properties.get("rule"));
     }
     if (properties.containsKey("table"))
       this.table = ((String)properties.get("table"));
     if (properties.containsKey("enable")) {
       this.enable = "true".equalsIgnoreCase(properties.get("enable").toString());
     }
     
     if (properties.containsKey("triger")) {
       this.triger = "true".equalsIgnoreCase(properties.get("triger").toString());
     }
   }
 }
