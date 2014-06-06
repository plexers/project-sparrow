package biz.plexers.sparrow.mp;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Action.Serializer.class)
public class Action {
	private String name;
	private int assignedCrew;
	private Choices type;

	public Action(Choices type, int assignedCrew) {
		this.assignedCrew = assignedCrew;
		this.type = type;
		this.name = type.name();
	}

	public enum Choices {
		AttackUsingCannons, LoadCannons, RepairShip
	}

	public Choices getType() {
		return type;
	}

	private Action(Map<String, Object> props) {
		name = (String) props.get("name");
		assignedCrew = (int) props.get("assignedCrew");
		type = DbHelper.mapAsObject(props.get("type"), Action.Choices.class);
	}

	@JsonCreator
	public static Action factory(Map<String, Object> props) {
		return new Action(props);
	}

	public static class Serializer extends JsonSerializer<Action> {

		@Override
		public void serialize(Action value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			jgen.writeStringField("name", value.name);
			jgen.writeNumberField("assignedCrew", value.assignedCrew);
			jgen.writeObjectField("type", value.type);
			
			jgen.writeEndObject();

		}

	}
}
