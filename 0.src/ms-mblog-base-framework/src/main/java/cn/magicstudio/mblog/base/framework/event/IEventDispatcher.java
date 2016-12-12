package cn.magicstudio.mblog.base.framework.event;

public  interface IEventDispatcher
{
  public  void register(Class<? extends Enum> paramClass, IEventHandler<? extends IEvent> paramIEventHandler);
  
  public  boolean exist(Class<? extends Enum> paramClass);
  
  public  void remove(Class<? extends Enum> paramClass, IEventHandler<IEvent> paramIEventHandler);
  
  public  void dispatch(IEvent paramIEvent);
  
  public  void clear();
}