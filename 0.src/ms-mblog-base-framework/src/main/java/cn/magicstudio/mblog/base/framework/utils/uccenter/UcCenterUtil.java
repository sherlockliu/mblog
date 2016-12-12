package cn.magicstudio.mblog.base.framework.utils.uccenter;

import com.yougou.logistics.base.common.enums.CacheTypeEnum;
import com.yougou.logistics.base.common.utils.CacheManage;
import com.yougou.logistics.base.common.vo.SignCertVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;

public class UcCenterUtil {
	private static final String CASHE_KEY_PRE = "MENU_TREE_CASHE_";

	public static final boolean casheMenuTree(String menuTree, String sessionid) {
		String casheKey = "MENU_TREE_CASHE_" + sessionid;
		CacheManage.put(casheKey, menuTree,
				CacheTypeEnum.AUTHORITY_MENU_TREE_QUERY);
		return true;
	}

	public static final String getMenuTreeFromCashe(String sessionid) {
		String casheKey = "MENU_TREE_CASHE_" + sessionid;
		return (String) CacheManage.getAndProLong(casheKey,
				CacheTypeEnum.AUTHORITY_MENU_TREE_QUERY);
	}

	public static final String removeMenuTreeFromCashe(String sessionid) {
		String casheKey = "MENU_TREE_CASHE_" + sessionid;
		CacheManage.remove(casheKey, CacheTypeEnum.AUTHORITY_MENU_TREE_QUERY);
		return "";
	}

	public static final String makeTicket(Object user) {
		return TicketUtil.makeTicket(user);
	}

	public static final <T> T parseTicket(String ticket, Class<T> valueType) {
		return (T) TicketUtil.parseTicket(ticket, valueType);
	}

	public static final List parseRole(String roleList, Class valueType) {
		try {
			byte[] all = Base64.decodeBase64(roleList);
			String json = new String(all);

			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.getSerializationConfig().setSerializationInclusion(
//					JsonSerialize.Inclusion.NON_NULL);  TODO
			TypeFactory t = TypeFactory.defaultInstance();
			return (List) objectMapper.readValue(json,
					t.constructCollectionType(ArrayList.class, valueType));
		} catch (Exception e) {
		}
		return null;
	}

	public static final SignCertVo genSignCertVo(String appCode, String appKey,
			Date date) {
		return SignCertSystemUtil.genSignCertVo(appCode, appKey, date);
	}

	public static final boolean verifySignCert(String appCode, String appKey,
			String date, String oldSign) {
		SignCertVo signCertVo = SignCertSystemUtil.genSignCertVo(appCode,
				appKey, date);
		String sign = signCertVo.getSign();
		if ((sign != null) && (sign.equals(oldSign))) {
			return true;
		}
		return false;
	}
}
