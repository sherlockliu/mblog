 package cn.magicstudio.mblog.data.core;
 
 import cn.wonhigh.retail.backend.security.Authorization;
 import com.yougou.logistics.base.common.model.SystemUser;
 import org.apache.commons.lang.StringUtils;
 
 public class OrganTypeDataSourceSwicher
   extends ThreadLocalDataSourceSwicher
 {
   public Object determineCurrentLookupKey()
   {
     SystemUser user = Authorization.getUser();
     
     String type = getDatasourceType();
     
     type = StringUtils.isEmpty(type) ? "" : type;
     if (user != null) {
       return type + user.getOrganTypeNo();
     }
     return type + (String)contextHolder.get();
   }
}