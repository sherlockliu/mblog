package cn.magicstudio.mblog.security;

import cn.wonhigh.retail.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.SystemUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmptyAuthorizationProvider implements AuthorizationProvider {
	public Map<String, AuthorityUserModuleDto> getUserModel()
			throws ManagerException {
		return null;
	}

	public Map<String, Integer> getOptions() {
		return null;
	}

	public String getCurrentModule() {
		return null;
	}

	public SystemUser getUser() {
		return null;
	}

	public SystemUser getUser(Integer userId) {
		return null;
	}

	public void setUser(SystemUser user) {
	}

	public int getSystemId() {
		return 0;
	}

	public void setSystemId(int val) {
	}

	public int getAreasystemId() {
		return 0;
	}

	public void setAreasystemId(int val) {
	}

	public void signout() {
	}

	public void clear() {
	}

	public AuthorityUserModuleDto getUserPermission() {
		return null;
	}

	public List<String> getRoles() {
		return new ArrayList();
	}
}
