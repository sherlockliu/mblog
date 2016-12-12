package cn.magicstudio.mblog.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

@Component
public class JsonDateSerializer$10 extends JsonSerializer<Date> {
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(value);
		jgen.writeString(formattedDate);
	}
}