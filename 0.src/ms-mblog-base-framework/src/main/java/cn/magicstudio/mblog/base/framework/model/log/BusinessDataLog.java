package cn.magicstudio.mblog.base.framework.model.log;

import cn.magicstudio.mblog.base.framework.model.LogBase;
import cn.magicstudio.mblog.base.frameworkn.enums.log.JsonType;
import cn.magicstudio.mblog.base.frameworkn.enums.log.OperType;
import cn.magicstudio.mblog.base.frameworkn.enums.log.SystemCode;

public class BusinessDataLog extends LogBase {
	private static final long serialVersionUID = 7634103801750169114L;

	public BusinessDataLog(SystemCode systemCode, int targetType) {
		setSystemCode(systemCode);
		setTargetType(targetType);
		setJsonType(JsonType.CUSTOM);
		setOperType(OperType.USER);
	}

	public BusinessDataLog() {
	}
}
