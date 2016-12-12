package cn.magicstudio.mblog.base.framework.utils;

import com.yougou.logistics.base.common.model.SystemUser;

public class ThreadLocalSystemUserVar {
	private static ThreadLocal<SystemUser> local = new ThreadLocal();

	public static SystemUser get() {
		return (SystemUser) local.get();
	}

	public static void set(SystemUser t) {
		local.set(t);
	}

	public static void remove() {
		local.remove();
	}
}
