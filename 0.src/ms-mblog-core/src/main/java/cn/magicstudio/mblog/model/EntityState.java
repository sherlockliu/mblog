package cn.magicstudio.mblog.model;

import java.io.Serializable;
import java.util.Date;

public abstract interface EntityState extends Serializable {
	public abstract Date getUpdateTime();

	public abstract void setUpdateTime(Date paramDate);

	public abstract Date getCreateTime();

	public abstract void setCreateTime(Date paramDate);
}
