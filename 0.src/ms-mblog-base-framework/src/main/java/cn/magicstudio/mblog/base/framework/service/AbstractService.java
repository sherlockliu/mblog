package cn.magicstudio.mblog.base.framework.service;

import java.util.Properties;

public abstract class AbstractService implements IService {
	protected IService.STATE state;

	public void init(Properties properties) {
	}

	public void start() {
	}

	public void stop() {
	}

	public void close() {
	}

	public void registerServiceListener(IServiceStateChangeListener listener) {
	}

	public void unregisterServiceListener(IServiceStateChangeListener listener) {
	}

	public String getName() {
		return null;
	}

	public IService.STATE getServiceState() {
		return this.state;
	}
}
