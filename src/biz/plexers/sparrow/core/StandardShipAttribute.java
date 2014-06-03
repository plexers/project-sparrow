package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StandardShipAttribute extends ShipAttribute{
	private Choices type;
	
	public enum Choices {
		MaxCannons, MaxCrew, MaxHealth
	}
	
	private StandardShipAttribute(Map<String, Object> props) {
		super(props);
		type = (Choices) props.get("type");
	}

	@JsonCreator
	public static StandardShipAttribute factory(Map<String, Object> props) {
		return new StandardShipAttribute(props);
	}

	public static class Serializer extends
			JsonSerializer<StandardShipAttribute> {

		@Override
		public void serialize(StandardShipAttribute value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			value.superSerialize(jgen);
			jgen.writeObjectField("type", value.type);

			jgen.writeEndObject();

		}

	}
}
