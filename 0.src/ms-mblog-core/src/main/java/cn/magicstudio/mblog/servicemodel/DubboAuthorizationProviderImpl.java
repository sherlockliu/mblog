package cn.magicstudio.mblog.servicemodel;

import cn.wonhigh.retail.backend.core.Storage;
import cn.wonhigh.retail.backend.security.Authorization;
import cn.wonhigh.retail.backend.security.AuthorizationProvider;
import cn.wonhigh.retail.uc.common.api.dto.AuthorityUserModuleDto;
import cn.wonhigh.retail.uc.common.api.model.AuthorityRole;
import cn.wonhigh.retail.uc.common.api.model.AuthorityUser;
import cn.wonhigh.retail.uc.common.api.service.AuthorityUserApi;
import com.alibaba.dubbo.rpc.RpcContext;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.model.SystemUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DubboAuthorizationProviderImpl
  implements AuthorizationProvider
{
  protected static final XLogger logger = XLoggerFactory.getXLogger(DubboAuthorizationProviderImpl.class);
  
  @Autowired(required=false)
  private AuthorityUserApi userApi;
  
  public Map<String, AuthorityUserModuleDto> getUserModel()
    throws ManagerException
  {
    throw new NotImplementedException();
  }

  public Map<String, Integer> getOptions()
  {
    throw new NotImplementedException();
  }
  
  public String getCurrentModule()
  {
    throw new NotImplementedException();
  }
  
  public SystemUser getUser()
  {
    return (SystemUser)RpcContext.getContext().get("user");
  }
  
  public SystemUser getUser(Integer userId)
  {
    SystemUser user = new SystemUser();
    try {
      AuthorityUser u = this.userApi.findAuthorityUserById(userId.intValue());
      user.setLoginName(u.getLoginName());
      user.setOrganizNo(u.getOrganNo());
      user.setOrganTypeNo(u.getOrganTypeNo());
      user.setOrganLevel(u.getOrganLevel());
      user.setUsername(u.getUserName());
    }
    catch (RpcException e) {
      logger.error("获取用户信息错误", e);
    }
    user.setUserid(userId);
    return user;
  }
  
  public void setUser(SystemUser user)
  {
    RpcContext.getContext().set("user", user);
  }
  
  public int getSystemId()
  {
    Object val = RpcContext.getContext().get("systemId");
    if (val == null)
      return 0;
    return Integer.parseInt(val.toString());
  }
  
  public void setSystemId(int val)
  {
    RpcContext.getContext().set("systemId", Integer.valueOf(val));
  }
  
  public int getAreasystemId()
  {
    Object val = RpcContext.getContext().get("AreasystemId");
    if (val == null)
      return 0;
    return Integer.parseInt(val.toString());
  }
  
  public void setAreasystemId(int val)
  {
    RpcContext.getContext().set("AreasystemId", Integer.valueOf(val));
  }
  


  public void signout() {}
  

  public void clear()
  {
    RpcContext.getContext().remove("user");
    RpcContext.getContext().remove("systemId");
    RpcContext.getContext().remove("AreasystemId");
  }
  
  public AuthorityUserModuleDto getUserPermission()
  {
    throw new NotImplementedException();
  }
  

  public List<String> getRoles()
  {
    Storage store = Authorization.getStorage();
    List<String> roles = null;
    roles = (List)store.get("roles");
    if (roles == null) {
      int userid = getUser().getUserid().intValue();
      roles = new ArrayList();
      try
      {
        List<AuthorityRole> items = this.userApi.findAuthorityUserRole(userid);
        for (AuthorityRole item : items) {
          roles.add(item.getRoleCode().toString());
        }
      } catch (RpcException e) {
        logger.error("获取用户角色信息异常", e);
      }
      store.set("user_roles", roles);
    }
    return roles;
  }
}
