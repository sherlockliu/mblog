package cn.magicstudio.mblog.base.framework.event;

public interface IEventHandler<T extends IEvent<?>>
{
  public void handle(T paramT);
}