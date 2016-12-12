package cn.magicstudio.mblog.base.framework.service;

import java.util.Properties;

public abstract interface IService {
	public abstract void init(Properties paramProperties);

	public abstract void start();

	public abstract void stop();

	public abstract void close();

	public abstract void registerServiceListener(
			IServiceStateChangeListener paramIServiceStateChangeListener);

	public abstract void unregisterServiceListener(
			IServiceStateChangeListener paramIServiceStateChangeListener);

	public abstract String getName();

	public abstract STATE getServiceState();

	public static enum STATE {
		NOTINITED(0, "NOTINITED"),

		INITED(1, "INITED"),

		STARTED(2, "STARTED"),

		STOPPED(3, "STOPPED");

		private final int value;

		private final String statename;

		private STATE(int value, String name) {
			this.value = value;
			this.statename = name;
		}

		public int getValue() {
			return this.value;
		}

		public String toString() {
			return this.statename;
		}
	}
}
