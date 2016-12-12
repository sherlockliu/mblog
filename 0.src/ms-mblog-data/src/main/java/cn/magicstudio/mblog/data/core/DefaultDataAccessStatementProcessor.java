 package cn.magicstudio.mblog.data.core;
 
 import cn.wonhigh.retail.backend.core.SpringContext;
 import cn.wonhigh.retail.backend.security.Authorization;
 import cn.wonhigh.retail.backend.security.DataAccessEnum;
 import cn.wonhigh.retail.backend.security.DataAccessProvider;
 import cn.wonhigh.retail.backend.security.EmptyDataAccessProvider;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Scanner;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.commons.lang.StringUtils;
 import org.apache.ibatis.mapping.BoundSql;
 import org.apache.ibatis.mapping.MappedStatement;
 import org.apache.ibatis.mapping.ResultMap;
 import org.apache.ibatis.mapping.ResultMapping;
 import org.apache.ibatis.reflection.MetaObject;
 import org.springframework.stereotype.Component;

 
 @Component("dataAccessStatementProcessor")
 public class DefaultDataAccessStatementProcessor
   implements DataAccessStatementProcessor
 {
   private boolean shopMode;
   private int max_size = 800;
   
   private Pattern regex = Pattern.compile("((--)?\\W+(AND|OR)?\\W?\\(?\\W?)?@(\\w+\\.)?(\\w+)(!\\w+)?", 2);
   
   private Pattern regexAutoWired = Pattern.compile("--\\W?+@AutoWired+\\W?(\\w+)?", 2);
   
   private DataAccessProvider dataAccessProvider;
   
   private String[] fields = { DataAccessEnum.BRAND, DataAccessEnum.ORDERUNIT, DataAccessEnum.SHOP, DataAccessEnum.STORE, DataAccessEnum.SUPPLIER, DataAccessEnum.ORGAN };
   
   public DefaultDataAccessStatementProcessor(boolean shopMode)
   {
     String val = System.getProperty("dataaccess.size");
     if (!StringUtils.isEmpty(val))
       this.max_size = Math.max(1, Integer.parseInt(val));
     this.shopMode = shopMode;
   }
   
   public String Process(MetaObject metaObject)
   {
     BoundSql boundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
     String script = boundSql.getSql();
     if ((StringUtils.indexOfIgnoreCase(script, "INSERT INTO") >= 0) || (StringUtils.indexOfIgnoreCase(script, "UPDATE ") >= 0) || (null == script))
     {
       return null;
     }
     
     if (script.indexOf("@AutoWired") > 0) {
       return ProcessAutoWired(script, metaObject);
     }
     return scan(script);
   }
   
   public String scan(String script) {
     String rn = System.getProperty("line.separator");
     Scanner scanner = new Scanner(script);
     StringBuffer sb = new StringBuffer();
     boolean changed = false;
     while (scanner.hasNextLine()) {
       String line = scanner.nextLine();
       line = line.trim();
       if (line.length() != 0)
       {
         String tmp = replace(line);
         if (tmp == null) {
           sb.append(line);
         } else {
           sb.append(tmp);
           changed = true;
         }
         sb.append(rn);
       } }
     if (changed) {
       return sb.toString();
     }
     return null;
   }
   
   public String replace(String script) {
     Matcher m = this.regex.matcher(script);
     StringBuffer sb = new StringBuffer();
     boolean finded = m.find();
     if (!finded) {
       return null;
     }
     while (finded)
     {
       String op = m.group(1);
       String alias = m.group(4);
       String name = m.group(5);
       String field = m.group(6);
       if (field == null) {
         field = name;
       } else
         field = field.substring(1);
       if (null == alias)
         alias = "";
       if (op == null) {
         op = "";
       } else
         op = op.replaceFirst("--", "");
       String statement = getStatement(alias, field, name);
       if (validate(name, statement)) {
         statement = op + " 1=1 ";
       }
       else
       {
         statement = op + " " + statement; }
       m.appendReplacement(sb, statement);
       finded = m.find();
     }
     m.appendTail(sb);
     String result = sb.toString() + System.getProperty("line.separator");
     return result;
   }
   
   private boolean validate(String name, String access) {
     try {
       String zone = Authorization.getCurrentZone();
       if ((!name.equalsIgnoreCase("brand_no")) && ("Z".equalsIgnoreCase(zone)) && (DbHelper.existOracle())) {
         return true;
       }
       return null == access;
     }
     catch (Exception e) {
       if (null == access) tmpTernaryOp = true; } return false;
   }
   
 
   private String ProcessAutoWired(String script, MetaObject metaObject)
   {
     MappedStatement mapper = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
     Matcher m = this.regexAutoWired.matcher(script);
     if (!m.find())
       return null;
     String alias = m.group(1);
     if (null == alias) {
       alias = "";
     } else {
       alias = alias + ".";
     }
     List<ResultMapping> columns = ((ResultMap)mapper.getResultMaps().get(0)).getResultMappings();
     List<String> where = new ArrayList();
     for (ResultMapping item : columns) {
       String column = item.getColumn();
       String name = getField(column);
       if (null != name)
       {
         String statement = getStatement(alias, column, name);
         if (null != statement)
           where.add(statement);
       }
     }
     if (where.size() == 0)
       return null;
     String express = " (" + StringUtils.join(where, " AND ") + ") ";
     
     int index = StringUtils.lastIndexOfIgnoreCase(script, "where");
     if (index <= 0) {
       return " Where " + express;
     }
     index += 5;
     String p1 = script.substring(0, index);
     String p2 = script.substring(index);
     return p1 + express + " AND" + p2 + System.getProperty("line.separator");
   }
   
   private String getField(String col)
   {
     for (String item : this.fields) {
       if ((col.startsWith(item)) || (col.endsWith(item)))
         return item;
     }
     return null;
   }
   
   private EmptyDataAccessProvider emptyDataAccessProvider = new EmptyDataAccessProvider();
   
   private DataAccessProvider getProvider()
   {
     if (this.dataAccessProvider == null) {
       if (SpringContext.containsBean("dataAccessProvider")) {
         this.dataAccessProvider = ((DataAccessProvider)SpringContext.getBean("dataAccessProvider"));
       } else
         return this.emptyDataAccessProvider;
     }
     return this.dataAccessProvider;
   }
   
   final String shopFlag = " in (select shop_no from shop_brand where (%s) )";
   
   private String getStatement(String alias, String column, String name)
   {
     if ((this.shopMode) && (DataAccessEnum.SHOP.equalsIgnoreCase(name))) {
       String orderUnit = splitDataString("", "order_unit_no", DataAccessEnum.ORDERUNIT);
       if (orderUnit == null)
         return null;
       return alias + column + String.format(" in (select shop_no from shop_brand where (%s) )", new Object[] { orderUnit });
     }
     return splitDataString(alias, column, name);
   }
   
   private String splitDataString(String alias, String column, String name)
   {
     List<String> vals = getProvider().getAccessData(name);
     if (vals == null)
       return null;
     if ((vals.size() > this.max_size) && (this.max_size > 5)) {
       List<String> lst = new ArrayList();
       List<String> tmp = new ArrayList();
       for (int i = 0; i < vals.size(); i++) {
         tmp.add("'" + (String)vals.get(i) + "'");
         if (tmp.size() == this.max_size) {
           lst.add(alias + column + " in (" + StringUtils.join(tmp, ",") + ")");
           tmp.clear();
         }
       }
       if (tmp.size() > 0) {
         lst.add(alias + column + " in (" + StringUtils.join(tmp, ",") + ")");
       }
       return "(" + StringUtils.join(lst, " OR ") + ")";
     }
     
     String script = getProvider().getAccessDataString(name);
     if (script == null)
       return null;
     return alias + column + " in (" + script + ")";
   }
 }