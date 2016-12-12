package cn.magicstudio.mblog.base.framework.event;

public interface IEvent<T extends Enum<T>> {
	
	public void setType(T paramTYPE);

	public T getType();

	public Object getTarget();

	public void setTarget(Object paramObject);

	public Object getData();

	public void setData(Object paramObject);

	public Object getParameter(String paramString);

	public void setParameter(String paramString, Object paramObject);
}
