package cn.magicstudio.mblog.security;

import cn.wonhigh.retail.backend.core.SpringContext;
import cn.wonhigh.retail.backend.core.Storage;
import com.yougou.logistics.base.common.model.SystemUser;
import org.springframework.stereotype.Component;

@Component("securityStorage")
public class SecurityStorage implements Storage {
	private String getKey(String key) {
		SystemUser user = Authorization.getUser();
		if (user == null)
			return key;
		return "user:s:" + key + ":" + user.getUserid();
	}

	public void set(String key, Object value) {
		key = getKey(key);
		getStorage().set(key, value);
	}

	public <T> T get(String key) {
		key = getKey(key);
		return (T) getStorage().get(key);
	}

	public void remove(String key) {
		key = getKey(key);
		getStorage().remove(key);
	}

	private Storage getStorage() {
		return (Storage) SpringContext.getBean("redisStorage");
	}

	public void set(String key, Object value, long expire) {
	}
}
