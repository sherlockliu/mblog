package cn.magicstudio.mblog.base.framework.utils.uccenter;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class TicketUtil {
	public static final String makeTicket(Object user) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.getSerializationConfig().setSerializationInclusion(
//					JsonSerialize.Inclusion.NON_NULL); TODO 
			String json = objectMapper.writeValueAsString(user);
			return Base64.encodeBase64String(json.getBytes());
		} catch (Exception e) {
		}
		return null;
	}

	public static final <T> T parseTicket(String ticket, Class<T> valueType) {
		try {
			byte[] all = Base64.decodeBase64(ticket);
			String json = new String(all, "UTF-8");

			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.getSerializationConfig().setSerializationInclusion(
//					JsonSerialize.Inclusion.NON_NULL); TODO

			return (T) objectMapper.readValue(json, valueType);
		} catch (Exception e) {
		}
		return null;
	}
}
