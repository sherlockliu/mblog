package cn.magicstudio.mblog.core;

public abstract interface Storage
{
  public abstract void set(String paramString, Object paramObject);
  
  public abstract void set(String paramString, Object paramObject, long paramLong);
  
  public abstract <T> T get(String paramString);
  
  public abstract void remove(String paramString);
}