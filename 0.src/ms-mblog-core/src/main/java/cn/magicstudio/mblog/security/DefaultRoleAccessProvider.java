package cn.magicstudio.mblog.security;

import com.yougou.logistics.base.common.model.SystemUser;

public class DefaultRoleAccessProvider implements RoleAccessProvider {
	public boolean hasPermission(String name) {
		SystemUser user = Authorization.getUser();
		if (user == null)
			return true;
		return false;
	}

	public void clear() {
	}
}
