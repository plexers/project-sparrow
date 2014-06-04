package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class UpgradableShipAttribute extends ShipAttribute {

	public enum Choices {
		Cannons, Crew, Armor, Health
	}

	private Choices type;

	public UpgradableShipAttribute(UpgradableShipAttribute.Choices choice, int value) {
		super(value);
		name = choice.name();
		type = choice;
	}

	private UpgradableShipAttribute(Map<String, Object> props) {
		super(props);
		type = (Choices) props.get("type");
	}

	@JsonCreator
	public static UpgradableShipAttribute factory(Map<String, Object> props) {
		return new UpgradableShipAttribute(props);
	}

	public static class Serializer extends
			JsonSerializer<UpgradableShipAttribute> {

		@Override
		public void serialize(UpgradableShipAttribute value,
				JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeStartObject();

			value.superSerialize(jgen);
			jgen.writeObjectField("type", value.type);

			jgen.writeEndObject();

		}

	}
}
