package cn.magicstudio.mblog.data.base.database;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.CodingRule;
import org.apache.ibatis.annotations.Param;

public abstract interface CodingRuleMapper
  extends BaseCrudMapper
{
  public abstract CodingRule selectByPrimaryKey(int paramInt)
    throws DaoException;
  
  public abstract CodingRule selectByRequestId(String paramString)
    throws DaoException;
  
  public abstract int resetCurrentSerialNo(@Param("requestId") String paramString, @Param("step") int paramInt)
    throws DaoException;
  
  public abstract int increaseSerialNo(@Param("requestId") String paramString, @Param("step") int paramInt)
    throws DaoException;
}