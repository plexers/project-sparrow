package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class InBattleShipAttribute extends ShipAttribute {

	public enum Choices {
		WoundedCrew, LoadedCannons
	}

	private Choices type;

	private InBattleShipAttribute(Map<String, Object> props) {
		super(props);
		type = DbHelper.mapAsObject(props.get("type"),InBattleShipAttribute.Choices.class);
	}

	@JsonCreator
	public static InBattleShipAttribute factory(Map<String, Object> props) {
		return new InBattleShipAttribute(props);
	}

	public static class Serializer extends
			JsonSerializer<InBattleShipAttribute> {

		@Override
		public void serialize(InBattleShipAttribute value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			value.superSerialize(jgen);
			jgen.writeObjectField("type", value.type);

			jgen.writeEndObject();

		}

	}
}
