package cn.magicstudio.mblog.base.framework.model;

import com.yougou.logistics.base.common.model.CodingRuleBuilder;

public abstract interface CodingRuleBuilderExt extends CodingRuleBuilder {
	public abstract void setSubRequestId(String paramString);

	public abstract String getSubRequestId();
}