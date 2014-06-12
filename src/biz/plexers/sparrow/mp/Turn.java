package biz.plexers.sparrow.mp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.mp.Action.Choices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Turn.Serializer.class)
public class Turn {
	Map<Action.Choices, Action> actions;

	public Turn() {
		actions = new HashMap<Choices, Action>();
	}

	public void addAction(Action a) {
		actions.put(a.getType(), a);
	}

	public int getCrewRequirements() {
		int total = 0;
		for (Action a : actions.values()) {
			total += a.getAssignedCrew();
		}

		return total;
	}

	private Turn(Map<String, Object> props) {
		actions = DbHelper.mapAsObject(props.get("actions"),
				new TypeReference<Map<Action.Choices, Action>>() {
				});
	}

	@JsonCreator
	public static Turn factory(Map<String, Object> props) {
		return new Turn(props);
	}

	public static class Serializer extends JsonSerializer<Turn> {

		@Override
		public void serialize(Turn value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			jgen.writeObjectField("actions", value.actions);

			jgen.writeEndObject();

		}

	}
}
