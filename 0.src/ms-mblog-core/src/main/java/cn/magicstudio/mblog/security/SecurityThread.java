package cn.magicstudio.mblog.security;

import com.yougou.logistics.base.common.model.SystemUser;

public class SecurityThread extends Thread {
	SystemUser user;

	public SecurityThread() {
		load();
	}

	public SecurityThread(Runnable task) {
		super(task);
		load();
	}

	public void init() {
		Authorization.setUser(this.user);
	}
	public void run() {
		init();
		super.run();
	}

	private void load() {
		this.user = Authorization.getUser();
	}
}
