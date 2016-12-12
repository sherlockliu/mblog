package cn.magicstudio.mblog.security;

import cn.wonhigh.retail.backend.core.ApplicationContext;
import cn.wonhigh.retail.backend.core.SpringContext;
import cn.wonhigh.retail.backend.core.Storage;
import cn.wonhigh.retail.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.base.common.model.SystemUser;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class Authorization {
	private static Configration configration;
	private static AuthorizationProvider authorizationProvider = null;

	private static DataAccessProvider dataAccessProvider;
	protected static final XLogger logger = XLoggerFactory
			.getXLogger(Authorization.class);

	private static Storage storage;
	private static RoleAccessProvider roleProvider;
	static final String zone_no_key = "z_";

	private static AuthorizationProvider getProvider() {
		if ((authorizationProvider == null)
				&& (SpringContext.containsBean("authorizationProvider")))
			authorizationProvider = (AuthorizationProvider) SpringContext
					.getBean("authorizationProvider");
		return authorizationProvider;
	}

	public static Storage getStorage() {
		if (storage == null)
			storage = (Storage) SpringContext.getBean("securityStorage");
		return storage;
	}

	private static RoleAccessProvider getRoleProvider() {
		if (roleProvider == null)
			if (SpringContext.containsBean("roleAccessProvider")) {
				roleProvider = (RoleAccessProvider) SpringContext
						.getBean("roleAccessProvider");
			} else
				roleProvider = new DefaultRoleAccessProvider();
		return roleProvider;
	}

	private static Configration getConfig() {
		if (configration == null)
			configration = (Configration) SpringContext.getBean("configration");
		return configration;
	}

	private static DataAccessProvider getDataAccessProvider() {
		if (dataAccessProvider == null) {
			if (SpringContext.containsBean("dataAccessProvider")) {
				dataAccessProvider = (DataAccessProvider) SpringContext
						.getBean("dataAccessProvider");
			} else
				dataAccessProvider = new EmptyDataAccessProvider();
		}
		return dataAccessProvider;
	}

	public static SystemUser getUser(Integer userId) {
		if (getProvider() == null)
			return null;
		return getProvider().getUser(userId);
	}

	public static SystemUser getUser() {
		if (getProvider() == null)
			return null;
		SystemUser user = getProvider().getUser();

		if ((user == null) && (getConfig().sessionDisable > 0)) {
			logger.info("验证关闭，自动创建用户:" + getConfig().sessionDisable);
			user = new SystemUser();
			user.setUserid(Integer.valueOf(getConfig().sessionDisable));
			user.setUsername("超级管理员");
			getProvider().setUser(user);
			clear();
		}

		return user;
	}

	public static void setUser(SystemUser val) {
		if (getProvider() == null) {
			return;
		}
		clear();

		getProvider().setUser(val);
	}

	public static int getSystemId() {
		if (getProvider() == null)
			return 0;
		return getProvider().getSystemId();
	}

	public static void setSystemId(int val) {
		if (getProvider() == null)
			return;
		getProvider().setSystemId(val);
	}

	public static int getAreasystemId() {
		if (getProvider() == null)
			return 0;
		return getProvider().getAreasystemId();
	}

	public static void setAreasystemId(int val) {
		if (getProvider() == null)
			return;
		getProvider().setAreasystemId(val);
	}

	public static void signout() {
		if (getProvider() == null)
			return;
		getProvider().signout();
		clear();
	}

	private static void clear() {
		if (getProvider() == null)
			return;
		if (getUser() != null)
			setCurrentZone(null);
		getProvider().clear();
		getRoleProvider().clear();
		getDataAccessProvider().clear();
	}

	private static boolean valide() {
		if ((getConfig().dataaccess == 0) || (getConfig().sessionDisable > 0))
			return true;
		if (getProvider() == null)
			return false;
		String module = getProvider().getCurrentModule();
		if (org.springframework.util.StringUtils.hasLength(module)) {
			return false;
		}
		return true;
	}

	public static boolean hasRole(String role) {
		if (getProvider() == null)
			return false;
		return getProvider().getRoles().contains(role);
	}

	public static boolean hasPermission(int ove) {
		if (getProvider() == null)
			return true;
		if (valide())
			return true;
		List<String> opts = getOptions();
		if (opts == null) {
			return false;
		}
		return opts.contains(Integer.valueOf(ove));
	}

	public static boolean hasPermission(String name, String opt) {
		if (getProvider() == null) {
			return true;
		}
		if ((!org.springframework.util.StringUtils.hasLength(opt))
				|| (opt.equalsIgnoreCase("128"))) {
			return true;
		}
		if (valide()) {
			return true;
		}
		List<String> opts = getOptions();
		if (opts == null) {
			return false;
		}
		return opts.contains(opt);
	}

	public static boolean hasPermission(String option) {
		if (valide())
			return true;
		if (getProvider() == null)
			return true;
		AuthorityUserModuleDto um = getProvider().getUserPermission();
		if (um == null) {
			return false;
		}

		String userPermission = "," + um.getPermissions() + ",";
		return userPermission.indexOf(option + ",") >= 0;
	}

	public static boolean hasDataPermission(String name) {
		if (getProvider() == null)
			return true;
		return getRoleProvider().hasPermission(name);
	}

	public static List<String> getOptions() {
		if (getProvider() == null)
			return new ArrayList();
		AuthorityUserModuleDto um = getProvider().getUserPermission();
		if (um == null)
			return null;
		List<String> opts = um.getOperations();
		if (opts == null)
			return new ArrayList();
		return opts;
	}

	public static void setCurrentZone(String zoneNo) {
		ApplicationContext.current().setValue("zone", zoneNo);

		getStorage().set("z_", zoneNo);
	}

	public static String getCurrentZone() {
		String zone = (String) ApplicationContext.current().getValue("zone");
		if (zone != null)
			return zone;
		Object val = getStorage().get("z_");
		if (val == null) {
			return null;
		}
		return String.valueOf(val);
	}

	public static List<String> getAccessData(String name) {
		if ((getProvider() == null)
				|| ((getUser() != null) && ("wang.sj"
						.equalsIgnoreCase(getUser().getLoginName()))))
			return null;
		return getDataAccessProvider().getAccessData(name);
	}

	public static String getAccessDataString(String name) {
		return getAccessDataString(name, false);
	}

	public static String getAccessDataString(String name, boolean isCondition) {
		List<String> result = getAccessData(name);
		if (result == null)
			return null;
		if (!isCondition) {
			return org.apache.commons.lang.StringUtils.join(result, ",");
		}
		return "'" + org.apache.commons.lang.StringUtils.join(result, "','")
				+ "'";
	}
}
