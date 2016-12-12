 package cn.magicstudio.mblog.data.core;
 
 import cn.wonhigh.retail.backend.core.SpringContext;
 import com.google.common.base.Function;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.HashMap;
 import javax.annotation.Resource;
 import org.apache.ibatis.executor.SimpleExecutor;
 import org.apache.ibatis.executor.statement.StatementHandler;
 import org.apache.ibatis.logging.jdbc.ConnectionLogger;
 import org.apache.ibatis.mapping.BoundSql;
 import org.apache.ibatis.mapping.MappedStatement;
 import org.apache.ibatis.plugin.Invocation;
 import org.apache.ibatis.reflection.MetaObject;
 import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
 import org.apache.ibatis.reflection.factory.ObjectFactory;
 import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
 import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
 import org.apache.ibatis.session.Configuration;
 import org.apache.ibatis.session.RowBounds;
 import org.apache.ibatis.session.SqlSession;
 import org.apache.ibatis.session.SqlSessionFactory;
 import org.springframework.stereotype.Component;
 import org.springframework.util.ReflectionUtils;
 
 @Component
 public class DbHelper
   implements AutoCloseable
 {
   private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
   private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
   private String defaultDriver = "mysql";
   
   public String getDefaultDriver() { return this.defaultDriver; }
   
   public void setDefaultDriver(String val) {
     this.defaultDriver = val;
   }
   
   @Resource(name="sqlSessionFactoryForLogistics")
   SqlSessionFactory sqlSessionFactory;
   SqlSessionFactory oracleFactory;
   SqlSession session;
   static DbHelper current;
   public void setSqlSessionFactory(SqlSessionFactory factory)
   {
     this.sqlSessionFactory = factory;
   }
   
   public DbHelper()
   {
     if (current == null)
       current = this;
   }
   
   public static String wrapName(String name) {
     String[] tmp = name.split("_");
     if (tmp.length == 1)
       return name;
     String v = tmp[0].toLowerCase();
     for (int i = 1; i < tmp.length; i++) {
       if (tmp[i].length() > 1) {
         v = v + tmp[i].substring(0, 1).toUpperCase() + tmp[i].substring(1).toLowerCase();
       } else {
         v = v + tmp[i].toUpperCase();
       }
     }
     return v;
   }
   
   static String getField(String name, boolean wrapName) {
     if (!wrapName)
       return name;
     return wrapName(name);
   }
   
   public static MetaObject getMappedStatement(Invocation invocation)
   {
     StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
     
     MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
     
     while ((metaStatementHandler.hasGetter("h")) || (metaStatementHandler.hasGetter("target"))) {
       Object object = null;
       if (metaStatementHandler.hasGetter("h"))
         object = metaStatementHandler.getValue("h");
       if (metaStatementHandler.hasGetter("target"))
         object = metaStatementHandler.getValue("target");
       if (object != null) {
         metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
       }
     }
     
     return metaStatementHandler;
   }
   
   public ResultSet FastExcute(String statement, Object params) throws SQLException
   {
     innerClose();
     
     Configuration configuration = current.getSessionFactory(statement).getConfiguration();
     MappedStatement ms = configuration.getMappedStatement(statement);
     this.session = current.getSessionFactory(statement).openSession();
     
     BoundSql boundSql = ms.getBoundSql(params);
     SimpleExecutor excecutor = new SimpleExecutor(configuration, null);
     StatementHandler sthandler = configuration.newStatementHandler(excecutor, ms, params, RowBounds.DEFAULT, null, boundSql);
     
 
     this.con = ConnectionLogger.newInstance(this.session.getConnection(), ms.getStatementLog(), 0);
     Statement stmt = sthandler.prepare(this.con);
     if (!existOracle()) {
       stmt.setFetchSize(Integer.MIN_VALUE);
     }
     sthandler.parameterize(stmt);
     ((PreparedStatement)stmt).execute();
     this.rs = stmt.getResultSet();
     
     return this.rs;
   }
   
   private SqlSessionFactory getSessionFactory(String statement) {
     if (statement.indexOf("oracle") >= 0) {
       if (this.oracleFactory == null)
         this.oracleFactory = ((SqlSessionFactory)SpringContext.getBean("oracle.sqlSessionFactoryForLogistics"));
       return this.oracleFactory;
     }
     
     return this.sqlSessionFactory;
   }
   
   public static void FastExcute(String statement, Object params, boolean wrapeName, Function<Object, Boolean> handler)
     throws SQLException
   {
     SqlSession session = current.getSessionFactory(statement).openSession();
     Configuration configuration = current.getSessionFactory(statement).getConfiguration();
     
 
     MappedStatement ms = configuration.getMappedStatement(statement);
     BoundSql boundSql = ms.getBoundSql(params);
     SimpleExecutor excecutor = new SimpleExecutor(configuration, null);
     StatementHandler sthandler = configuration.newStatementHandler(excecutor, ms, params, RowBounds.DEFAULT, null, boundSql);
     
 
     ResultSet rs = null;
     Connection con = null;
     try {
       con = ConnectionLogger.newInstance(session.getConnection(), ms.getStatementLog(), 0);
 
       Statement stmt = sthandler.prepare(con);
       if (!existOracle()) {
         stmt.setFetchSize(Integer.MIN_VALUE);
       }
       sthandler.parameterize(stmt);
       ((PreparedStatement)stmt).execute();
       rs = stmt.getResultSet();
       java.sql.ResultSetMetaData meta = rs.getMetaData();
       
       String[] fields = getFields(meta, wrapeName);
       while (rs.next()) {
         HashMap<String, Object> vals = new HashMap();
         for (int i = 1; i <= fields.length; i++) {
           vals.put(fields[(i - 1)], rs.getObject(i));
         }
         if (!((Boolean)handler.apply(vals)).booleanValue()) {
           break;
         }
       }
     }
     finally {
       if (rs != null)
         rs.close();
       if (con != null) {
         con.close();
       }
       session.close();
     }
   }
   
   public static void FastExcute(String statement, Object params, Function<Object, Boolean> handler) throws SQLException
   {
     FastExcute(statement, params, true, handler);
   }
   
   public static String[] getFields(java.sql.ResultSetMetaData meta, boolean wrapName) throws SQLException {
     int fieldLength = meta.getColumnCount();
     String[] fields = new String[fieldLength];
     if ((meta instanceof com.mysql.jdbc.ResultSetMetaData)) {
       java.lang.reflect.Field f = null;
       try {
         f = com.mysql.jdbc.ResultSetMetaData.class.getDeclaredField("fields");
       } catch (Exception e) {
         e.printStackTrace();
       }
       f.setAccessible(true);
       com.mysql.jdbc.Field[] values = (com.mysql.jdbc.Field[])ReflectionUtils.getField(f, meta);
       for (int i = 0; i < values.length; i++) {
         com.mysql.jdbc.Field col = values[i];
         fields[i] = getField(col.getName(), wrapName);
       }
     }
     else {
       for (int i = 1; i <= fieldLength; i++) {
         String field = meta.getColumnName(i);
         fields[(i - 1)] = getField(field, wrapName);
       }
     }
     return fields;
   }
   
 
   private ResultSet rs = null;
   private Connection con = null;
   
   public void close() throws Exception
   {
     innerClose();
     if (this.session != null) {
       this.session.close();
     }
   }
   
   private void innerClose() throws SQLException {
     if (this.rs != null)
       this.rs.close();
     if (this.con != null)
       this.con.close();
   }
   
   public static boolean existOracle() {
     if (current.defaultDriver.equals("oracle"))
       return true;
     try {
       current.getSessionFactory("oracle");
       return current.oracleFactory != null;
     } catch (Exception e) {}
     return false;
   }
   
   public static <T> T getInterfaceMapper(Class<?> normal)
   {
     String name = normal.getSimpleName();
     if ((!existOracle()) && ("oracle".equalsIgnoreCase(current.defaultDriver)))
       return (T)SpringContext.getBean(normal);
     name = "oracle." + name;
     return (T)SpringContext.getBean(name);
   }
 }