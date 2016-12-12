package cn.magicstudio.mblog.data.core;

public abstract interface DataSourceSwicher
{
  public abstract Object determineCurrentLookupKey();
  
  public abstract void setCustomerType(String paramString);
  
  public abstract String getDatasourceType();
  
  public abstract void setDatasourceType(String paramString);
}