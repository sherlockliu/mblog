package cn.magicstudio.mblog.base.framework.model;

import java.io.Serializable;
import java.util.Date;

public class CodingRule implements Serializable {
	private Integer id;
	private String requestId;
	private String requestName;
	private String prefix;
	private Integer currentSerialNo;
	private Integer serialNoLength;
	private Integer resetMode;
	private Date resetTime;
	private Date dbTime;
	private Boolean allowBreakNo;
	private Boolean isAbstract;
	private String remarks;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestName() {
		return this.requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getCurrentSerialNo() {
		return this.currentSerialNo;
	}

	public void setCurrentSerialNo(Integer currentSerialNo) {
		this.currentSerialNo = currentSerialNo;
	}

	public Integer getSerialNoLength() {
		return this.serialNoLength;
	}

	public void setSerialNoLength(Integer serialNoLength) {
		this.serialNoLength = serialNoLength;
	}

	public Integer getResetMode() {
		return this.resetMode;
	}

	public void setResetMode(Integer resetMode) {
		this.resetMode = resetMode;
	}

	public Date getResetTime() {
		return this.resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public Date getDbTime() {
		return this.dbTime;
	}

	public void setDbTime(Date dbTime) {
		this.dbTime = dbTime;
	}

	public Boolean getAllowBreakNo() {
		return this.allowBreakNo;
	}

	public void setAllowBreakNo(Boolean allowBreakNo) {
		this.allowBreakNo = allowBreakNo;
	}

	public Boolean getIsAbstract() {
		return this.isAbstract;
	}

	public void setIsAbstract(Boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
