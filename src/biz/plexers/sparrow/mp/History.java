package biz.plexers.sparrow.mp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = History.Serializer.class)
public class History {
	List<Turn> historyList;
	
	public History() {
		historyList = new ArrayList<>();
	}
	
	public void pushTurn(Turn turn) {
		
	}
	
	private History(Map<String, Object> props) {
		Object objHistoryList = props.get("historyList");
		historyList = DbHelper.mapAsObject(objHistoryList, new TypeReference<List<Turn>>() {
		});
	}

	@JsonCreator
	public static History factory(Map<String, Object> props) {
		return new History(props);
	}

	public static class Serializer extends JsonSerializer<History> {

		@Override
		public void serialize(History value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			
			jgen.writeObjectField("historyList", value.historyList);
			
			jgen.writeEndObject();

		}

	}
}
