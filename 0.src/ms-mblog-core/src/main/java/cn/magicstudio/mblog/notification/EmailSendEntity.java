package cn.magicstudio.mblog.notification;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmailSendEntity implements Serializable {
	private static final long serialVersionUID = -443961126095688773L;
	private String businessSystemCode;
	private Integer priority;
	private Date scheduleSendDate;
	private Integer emailMsgType;
	private String emailSenderAddr;
	private String emailSenderNo;
	private String emailSenderName;
	private List<EmailReceiverEntity> mainReceiverEntityList;
	private List<EmailReceiverEntity> carbonReceiverEntityList;
	private String emailSubject;
	private byte[] emailContent;
	private byte[] emailAccessory;
	private Date createTime;

	public String getBusinessSystemCode() {
		return this.businessSystemCode;
	}

	public void setBusinessSystemCode(String businessSystemCode) {
		this.businessSystemCode = businessSystemCode;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getScheduleSendDate() {
		return this.scheduleSendDate;
	}

	public void setScheduleSendDate(Date scheduleSendDate) {
		this.scheduleSendDate = scheduleSendDate;
	}

	public Integer getEmailMsgType() {
		return this.emailMsgType;
	}

	public void setEmailMsgType(Integer emailMsgType) {
		this.emailMsgType = emailMsgType;
	}

	public String getEmailSenderAddr() {
		return this.emailSenderAddr;
	}

	public void setEmailSenderAddr(String emailSenderAddr) {
		this.emailSenderAddr = emailSenderAddr;
	}

	public String getEmailSenderNo() {
		return this.emailSenderNo;
	}

	public void setEmailSenderNo(String emailSenderNo) {
		this.emailSenderNo = emailSenderNo;
	}

	public String getEmailSenderName() {
		return this.emailSenderName;
	}

	public void setEmailSenderName(String emailSenderName) {
		this.emailSenderName = emailSenderName;
	}

	public List<EmailReceiverEntity> getMainReceiverEntityList() {
		return this.mainReceiverEntityList;
	}

	public void setMainReceiverEntityList(
			List<EmailReceiverEntity> mainReceiverEntityList) {
		this.mainReceiverEntityList = mainReceiverEntityList;
	}

	public List<EmailReceiverEntity> getCarbonReceiverEntityList() {
		return this.carbonReceiverEntityList;
	}

	public void setCarbonReceiverEntityList(
			List<EmailReceiverEntity> carbonReceiverEntityList) {
		this.carbonReceiverEntityList = carbonReceiverEntityList;
	}

	public String getEmailSubject() {
		return this.emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public byte[] getEmailContent() {
		return this.emailContent;
	}

	public void setEmailContent(byte[] emailContent) {
		this.emailContent = emailContent;
	}

	public byte[] getEmailAccessory() {
		return this.emailAccessory;
	}

	public void setEmailAccessory(byte[] emailAccessory) {
		this.emailAccessory = emailAccessory;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
