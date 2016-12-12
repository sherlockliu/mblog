package cn.magicstudio.mblog.data.core;

import cn.wonhigh.retail.backend.core.SpringContext;
import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Intercepts({@org.apache.ibatis.plugin.Signature(type=StatementHandler.class, method="prepare", args={java.sql.Connection.class})})
public class DataAccessInspector
  implements Interceptor
{
  protected static final Logger logger = LoggerFactory.getLogger(DataAccessInspector.class);
  
  DataAccessStatementProcessor statementProcessor;
  
  public DataAccessStatementProcessor getProcessor()
  {
    String name = "dataAccessStatementProcessor";
    if (this.statementProcessor != null) {
      return this.statementProcessor;
    }
    if (SpringContext.containsBean(name)) {
      this.statementProcessor = ((DataAccessStatementProcessor)SpringContext.getBean(name));
    }
    else {
      this.statementProcessor = new DefaultDataAccessStatementProcessor(this.shopMode);
    }
    return this.statementProcessor;
  }
  
  public Object intercept(Invocation invocation)
    throws Throwable
  {
    MetaObject metaObject = DbHelper.getMappedStatement(invocation);
    MappedStatement mapped = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
    
    String url = mapped.getId();
    String statement = getProcessor().Process(metaObject);
    
    if (null == statement) {
      return invocation.proceed();
    }
    metaObject.setValue("delegate.boundSql.sql", statement);
    return invocation.proceed();
  }
  

  public Object plugin(Object target)
  {
    if ((target instanceof StatementHandler)) {
      return Plugin.wrap(target, this);
    }
    return target;
  }
  

  private boolean shopMode = false;
  
  public void setProperties(Properties properties)
  {
    this.shopMode = ((properties.containsKey("shopMode")) && ("1".equals(properties.get("shopMode").toString())));
  }
}