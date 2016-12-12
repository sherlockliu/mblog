package cn.magicstudio.mblog.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public final class JsonUtils {
	private static ObjectMapper objectMapper = generateMapper(JsonSerialize.Inclusion.ALWAYS);

	public static <T> T fromJson(String json, Class<T> clazz)
			throws IOException {
		return (T) (clazz.equals(String.class) ? json : objectMapper.readValue(json,
				clazz));
	}

	public static JsonNode fromJson(String json) throws IOException {
		JsonNode node = objectMapper.readTree(json);

		return node;
	}

	public static <T> T fromJson(String json, TypeReference<?> typeReference)
			throws IOException {
		return (T) (typeReference.getType().equals(String.class) ? json
				: objectMapper.readValue(json, typeReference));
	}

	public static <T> List<T> fromListJson(String json, Class<T> clazz)
			throws IOException {
		return (List) objectMapper.readValue(json, objectMapper
				.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	public static JsonNode getNode(String json, String nodeName) {
		JsonNode node = null;
		try {
			node = objectMapper.readTree(json);
			return node.get(nodeName);
		} catch (Exception e) {
		}
		return node;
	}

	public static <T> T jsonNodeToObject(JsonNode node,
			TypeReference<T> typeReference) {
		if ((node == null) || ("".equals(node))) {
			return null;
		}
		try {
			return (T) objectMapper.readValue(node, typeReference);
		} catch (Exception localException) {
		}

		return null;
	}

	public static <T> String toJson(T src) throws IOException {
		return (src instanceof String) ? (String) src : objectMapper
				.writeValueAsString(src);
	}

	public static <T> String toJson(T src, JsonSerialize.Inclusion inclusion)
			throws IOException {
		if ((src instanceof String)) {
			return (String) src;
		}
		ObjectMapper customMapper = generateMapper(inclusion);
		return customMapper.writeValueAsString(src);
	}

	public static <T> String toJson(T src, ObjectMapper mapper)
			throws IOException {
		if (mapper != null) {
			if ((src instanceof String)) {
				return (String) src;
			}
			return mapper.writeValueAsString(src);
		}

		return null;
	}

	public static ObjectMapper mapper() {
		return objectMapper;
	}

	private static ObjectMapper generateMapper(JsonSerialize.Inclusion inclusion) {
		ObjectMapper customMapper = new ObjectMapper();
//
//		customMapper.setSerializationInclusion(inclusion);
//
//		customMapper
//				.configure(
//						DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
//						false);
//
//		customMapper.configure(
//				DeserializationFeature.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
//
//		customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); TODO
		return customMapper;
	}
 }