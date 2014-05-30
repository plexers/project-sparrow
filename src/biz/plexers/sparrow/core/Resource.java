package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Resource.Serializer.class)
public class Resource {

	private String name;
	private int quantity;
	private Choices type;

	public enum Choices {

		Lumber, Cannons, Crew, Metal
	}

	public void changeQ(int offset) {

		if (quantity > offset) {
			quantity += offset;
		}

	}

	public void consume(Resource other) {

		if (other != null) {
			if (this.type.equals(other)) {
				this.quantity += other.quantity;
			}
		}
	}

	private Resource(Map<String, Object> props) {
		name = (String) props.get("name");
		quantity = (int) props.get("quantity");
		type = (Choices) props.get("type");
	}

	@JsonCreator
	public static Resource factory(Map<String, Object> props) {
		return new Resource(props);
	}

	public class Serializer extends JsonSerializer<Resource> {

		@Override
		public void serialize(Resource value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			jgen.writeStringField("name", value.name);
			jgen.writeNumberField("quantity", value.quantity);
			jgen.writeObjectField("type", value.type);
			jgen.writeEndObject();

		}

	}
}
