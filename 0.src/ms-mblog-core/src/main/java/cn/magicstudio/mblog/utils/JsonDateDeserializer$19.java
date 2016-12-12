package cn.magicstudio.mblog.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class JsonDateDeserializer$19 extends JsonDeserializer<Date> {
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(jp.getText());
		} catch (Exception e) {
		}
		return null;
	}
}
