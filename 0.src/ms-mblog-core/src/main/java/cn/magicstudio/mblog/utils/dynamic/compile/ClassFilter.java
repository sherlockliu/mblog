package cn.magicstudio.mblog.utils.dynamic.compile;

public abstract interface ClassFilter
{
  public abstract boolean accept(Class<?> paramClass);
}