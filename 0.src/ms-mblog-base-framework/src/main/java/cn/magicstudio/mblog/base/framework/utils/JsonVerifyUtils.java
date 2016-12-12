package cn.magicstudio.mblog.base.framework.utils;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonVerifyUtils {
	private static Log logger = LogFactory.getLog(JsonVerifyUtils.class);

	public static boolean verify(String json) {
		return getJsonNode(json) != null;
	}

	public static JsonNode getJsonNode(String json) {
		if (StringUtils.isBlank(json)) {
			logger.error("+++++++ json 內容 为 空  -----------");
			return null;
		}

		try {
			return new ObjectMapper().readTree(json);
		} catch (JsonParseException e) {
			logger.error("JsonVerifyUtils 格式 JsonParseException ", e);
		} catch (IOException e) {
			logger.error("JsonVerifyUtils 格式 IOException ", e);
		}
		return null;
	}

	public static void main(String[] arg) {
		System.out.println(verify("{\"'a'\"  : 12}"));
	}
}
