package cn.magicstudio.mblog.base.framework.event;

public class BelleEvent<TYPE extends Enum<TYPE>> extends AbstractEvent<TYPE> {
	public BelleEvent() {
	}

	public BelleEvent(TYPE type) {
		super(type);
	}

	public BelleEvent(TYPE type, Object target, Object data) {
		super(type, target, data);
	}
}
