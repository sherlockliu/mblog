package cn.magicstudio.mblog.data.trigger;

public abstract interface DbTrigger
{
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract boolean match(Object... paramVarArgs);
  
  public abstract boolean trigger(Object paramObject);
}