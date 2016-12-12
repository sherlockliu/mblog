package cn.magicstudio.mblog.base.framework.model;

import java.io.Serializable;

public class JobBizLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private String triggerName;
	private String groupName;
	private String type;
	private Long gmtDate;
	private String remark;

	public JobBizLog() {
	}

	public JobBizLog(String triggerName, String groupName, String type,
			Long gmtDate, String remark) {
		this.triggerName = triggerName;
		this.groupName = groupName;
		this.type = type;
		this.gmtDate = gmtDate;
		this.remark = remark;
	}

	public String getTriggerName() {
		return this.triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getGmtDate() {
		return this.gmtDate;
	}

	public void setGmtDate(Long gmtDate) {
		this.gmtDate = gmtDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
