package cn.magicstudio.mblog.base.framework.model;

public class VpdUser {
	private String userId;

	private String moduleId;

	private String warehorseId;

	public VpdUser(String userId) {
		this.userId = userId;
	}

	public VpdUser(String userId, String moduleId) {
		this.userId = userId;
		this.moduleId = moduleId;
	}

	public VpdUser(String userId, String moduleId, String warehorseId) {
		this.userId = userId;
		this.moduleId = moduleId;
		this.warehorseId = warehorseId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getWarehorseId() {
		return this.warehorseId;
	}

	public void setWarehorseId(String warehorseId) {
		this.warehorseId = warehorseId;
	}
}