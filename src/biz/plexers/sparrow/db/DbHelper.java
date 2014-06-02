package biz.plexers.sparrow.db;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DbHelper {
	
	public static <E> E mapAsObject(Map<String, Object> map, Class<E> objectClass) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(map);
			return objectMapper.readValue(json, objectClass);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
