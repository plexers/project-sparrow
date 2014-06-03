package biz.plexers.sparrow.db;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
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
	
	public static <E> E mapAsObject(Map<String, Object> map, TypeReference<E> typeRef) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(map);
			return objectMapper.readValue(json, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static double objectToDouble(Object obj) {
		if(obj instanceof Integer) {
			Integer intObj= (Integer) obj;
			return intObj.doubleValue();
		}
		else if (obj instanceof Double) {
			Double doubleObj = (Double) obj;
			return doubleObj;
		}
		return 0;
	}
}
