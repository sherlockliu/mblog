package cn.magicstudio.mblog.base.framework.utils;

import com.yougou.logistics.base.common.model.VpdUser;

public class ThreadLocalVpdVar
{
  private static ThreadLocalVpdVar var = null;
  
  private static ThreadLocal<VpdUser> local = new ThreadLocal();
  
  public static synchronized ThreadLocalVpdVar getInitialize() {
    if (var == null) {
      var = new ThreadLocalVpdVar();
    }
    return var;
  }
  
  public VpdUser get() {
    return (VpdUser)local.get();
  }
  
  public void set(VpdUser t) {
    local.set(t);
  }
  
  public void remove() {
    local.remove();
  }
}
