package cn.magicstudio.mblog.base.framework.enums;

import java.util.Calendar;

public enum CacheExpireEnum {
	FIVE_MINUTE(300000L),

	TEN_MINUTE(600000L),

	THIRTY_MINUTE(1800000L),

	ONE_HOUR(3600000L),

	TWO_HOUR(7200000L),

	TWELVE_HOUR(43200000L),

	ONE_DAY(86400000L),

	TODAY(-1L);

	private long expire;

	private CacheExpireEnum(long expire) {
		this.expire = expire;
	}

	public long getExpire() {
		if (TODAY.equals(this)) {
			Calendar c = Calendar.getInstance();
			c.set(14, 0);
			c.set(13, 0);
			c.set(12, 0);
			c.set(11, 0);
			c.add(5, 1);
			return c.getTimeInMillis() - System.currentTimeMillis();
		}
		return this.expire;
	}
}
