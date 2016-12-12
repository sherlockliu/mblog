package cn.magicstudio.mblog.base.framework.model;

import com.yougou.logistics.base.common.model.CodingRuleBuilderExt;

public class DefaultCodingRuleBuilderExt implements CodingRuleBuilderExt {
	private String requestId;

	private String subRequestId;

	private String prefix;

	private String formatedTime;

	private String sequence;

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setFormatedTime(String formatedTime) {
		this.formatedTime = formatedTime;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String buildNo() {
		return this.prefix + this.formatedTime + this.sequence;
	}

	public void setSubRequestId(String subRequestId) {
		this.subRequestId = subRequestId;
	}

	public String getSubRequestId() {
		return this.subRequestId;
	}
}
