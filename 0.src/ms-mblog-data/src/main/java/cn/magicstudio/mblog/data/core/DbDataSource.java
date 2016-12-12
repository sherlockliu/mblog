package cn.magicstudio.mblog.data.core;

import cn.wonhigh.retail.backend.model.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DbDataSource implements AutoCloseable, DataSource<Map<String, Object>>
{
  DbIterator it;
  DbHelper helper;
  
  public DbDataSource(String statement, Object params) throws Exception
  {
    this.helper = new DbHelper();
    ResultSet rs = this.helper.FastExcute(statement, params);
    this.it = new DbIterator(rs);
  }
  
  public void close() throws Exception
  {
    if (this.it != null) {
      this.it.close();
    }
  }

  public Iterator<Map<String, Object>> getIterator() { return this.it; }
  
  protected class DbIterator implements Iterator<Map<String, Object>> {
    ResultSet rs;
    int fieldLength;
    String[] fields;
    boolean hasnext;
    
    public DbIterator(ResultSet rs) {
      this.rs = rs;
      ResultSetMetaData meta;
      try {
        meta = rs.getMetaData();
        this.fieldLength = meta.getColumnCount();
        this.fields = new String[this.fieldLength];
        for (int i = 1; i <= this.fieldLength; i++) {
          String field = meta.getColumnName(i);
          this.fields[(i - 1)] = getField(field);
        }
        
        this.hasnext = rs.next();
      } catch (SQLException e) {
        this.hasnext = false;
      }
    }
    
    public boolean hasNext()
    {
      return this.hasnext;
    }
    
    public Map<String, Object> next()
    {
      if (!this.hasnext)
        return null;
      HashMap<String, Object> vals = new HashMap();
      try {
        for (int i = 1; i <= this.fieldLength; i++) {
          vals.put(this.fields[(i - 1)], this.rs.getObject(i));
        }
        this.hasnext = this.rs.next();
      } catch (SQLException e) {
        throw new NullPointerException();
      }
      return vals;
    }
    

    public void remove() {}
    
    public void close()
      throws Exception
    {
      DbDataSource.this.helper.close();
    }
    
    private String getField(String name) {
      String[] tmp = name.split("_");
      if (tmp.length == 1)
        return name;
      String v = tmp[0];
      for (int i = 1; i < tmp.length; i++) {
        if (tmp[i].length() > 1) {
          v = v + tmp[i].substring(0, 1).toUpperCase() + tmp[i].substring(1);
        } else {
          v = v + tmp[i].toUpperCase();
        }
      }
      return v;
    }
  }
}