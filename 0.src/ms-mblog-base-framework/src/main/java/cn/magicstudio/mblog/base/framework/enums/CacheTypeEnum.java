package cn.magicstudio.mblog.base.framework.enums;

public enum CacheTypeEnum {
	AUTHORITY_BRAND_QUERY(CacheStateEnum.STATIC, CacheExpireEnum.THIRTY_MINUTE),

	AUTHORITY_ORGANIZATION_QUERY(CacheStateEnum.STATIC,
			CacheExpireEnum.THIRTY_MINUTE),

	AUTHORITY_MENU_TREE_QUERY(CacheStateEnum.LRU, CacheExpireEnum.TODAY),

	AUTHORITY_MODULE_QUERY(CacheStateEnum.STATIC, CacheExpireEnum.THIRTY_MINUTE);

	private CacheExpireEnum expire;
	private CacheStateEnum state;

	private CacheTypeEnum(CacheStateEnum state, CacheExpireEnum expire) {
		this.state = state;
		this.expire = expire;
	}

	public CacheExpireEnum getExpire() {
		return this.expire;
	}

	public CacheStateEnum getState() {
		return this.state;
	}
}
