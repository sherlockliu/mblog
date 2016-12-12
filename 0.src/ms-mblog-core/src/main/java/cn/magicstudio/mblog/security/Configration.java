package cn.magicstudio.mblog.security;

import org.springframework.beans.factory.annotation.Value;

@org.springframework.stereotype.Component("configration")
public class Configration {
	public int dataaccess;
	@Value("${session.disable:0}")
	public int sessionDisable;

	@Value("${dataaccess}")
	public void setDataaccess(int val) {
		this.dataaccess = val;
	}
}
