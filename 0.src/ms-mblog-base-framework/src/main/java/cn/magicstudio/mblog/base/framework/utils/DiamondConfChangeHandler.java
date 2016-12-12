package cn.magicstudio.mblog.base.framework.utils;

public abstract class DiamondConfChangeHandler {
	public void handler(String confPath, String propertyName, String content) {
		afterHandler(confPath, propertyName, content);
	}

	protected abstract void afterHandler(String paramString1,
			String paramString2, String paramString3);
}
