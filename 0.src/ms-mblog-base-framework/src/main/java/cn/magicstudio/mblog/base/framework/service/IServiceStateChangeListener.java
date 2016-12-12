package cn.magicstudio.mblog.base.framework.service;

import com.yougou.logistics.base.common.service.IService;

public abstract interface IServiceStateChangeListener {
	public abstract void stateChanged(IService paramIService);
}
