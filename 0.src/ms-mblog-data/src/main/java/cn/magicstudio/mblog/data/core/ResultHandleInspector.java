 package cn.magicstudio.mblog.data.core;
 
 import java.util.Properties;
 import javax.transaction.NotSupportedException;
 import org.apache.ibatis.plugin.Interceptor;
 import org.apache.ibatis.plugin.Intercepts;
 import org.apache.ibatis.plugin.Invocation;
 import org.apache.ibatis.plugin.Plugin;
 import org.apache.ibatis.session.ResultContext;
 import org.apache.ibatis.session.ResultHandler;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 @Intercepts({@org.apache.ibatis.plugin.Signature(type=ResultHandler.class, method="handleResult", args={ResultContext.class})})
 public class ResultHandleInspector
   implements Interceptor
 {
   protected static final Logger logger = LoggerFactory.getLogger(DataAccessInspector.class);
   
 
   private int default_max_count = 60000;
   private boolean env_release = true;
   
 
 
   public Object intercept(Invocation invocation)
     throws Throwable
   {
     ResultContext context = (ResultContext)invocation.getArgs()[0];
     if ((context.getResultCount() > this.default_max_count) && (!this.env_release))
       throw new NotSupportedException("兄弟数据量太大了，扛不住啊...");
     return invocation.proceed();
   }
   
   public Object plugin(Object target)
   {
     if ((target instanceof ResultHandler)) {
       return Plugin.wrap(target, this);
     }
     return target;
   }
 
   public void setProperties(Properties properties)
   {
     this.env_release = System.getProperty("env", "release").equals("release");
     String val = properties.getProperty("max", "");
     if (!val.equals("")) {
       this.default_max_count = Integer.parseInt(val);
     }
   }
 }