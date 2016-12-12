package cn.magicstudio.mblog.base.framework.enums;

public interface IndexEnum<E extends Enum<E>>
//public interface IndexEnum<E extends Enum<E>,  extends IndexEnum<E>>
{
  public int getIndex();
  
  public String getName();
}