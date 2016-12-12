package cn.magicstudio.mblog.base.framework.utils.uccenter;

import com.yougou.logistics.base.common.vo.SignCertVo;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.util.DigestUtils;

public final class SignCertSystemUtil {
	public static final SignCertVo genSignCertVo(String appCode, String appKey,
			Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String signDate = simpleDateFormat.format(date);
		return genSignCertVo(appCode, appKey, signDate);
	}

	public static final SignCertVo genSignCertVo(String appCode, String appKey,
			String date) {
		SignCertVo signCertVo = new SignCertVo();

		signCertVo.setAppCode(appCode);
		signCertVo.setAppKey(appKey);

		signCertVo.setSignDate(date);

		String sign = DigestUtils.md5DigestAsHex((appCode + appKey + date)
				.getBytes());
		signCertVo.setSign(sign);

		return signCertVo;
	}
}
