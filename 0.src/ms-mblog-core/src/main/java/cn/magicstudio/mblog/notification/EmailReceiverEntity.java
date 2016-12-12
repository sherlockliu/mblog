package cn.magicstudio.mblog.notification;

import java.io.Serializable;

public class EmailReceiverEntity implements Serializable {
	private static final long serialVersionUID = 5804410645735664505L;
	private String receiveEmailAddr;
	private String receiverNo;
	private String receiver;

	public String getReceiveEmailAddr() {
		return this.receiveEmailAddr;
	}

	public void setReceiveEmailAddr(String receiveEmailAddr) {
		this.receiveEmailAddr = receiveEmailAddr;
	}

	public String getReceiverNo() {
		return this.receiverNo;
	}

	public void setReceiverNo(String receiverNo) {
		this.receiverNo = receiverNo;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}
