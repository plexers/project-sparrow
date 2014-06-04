package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;

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
	
	private double unitPrice;

	public Resource(Choices type) {
		this.type = type;
		this.name = type.name();
	}

	public Resource(Choices type, int quantity) {
		this.quantity = quantity;
		this.type = type;
		this.name = type.name();
	}

	public enum Choices {

		Lumber, Cannons, Crew, Metal
	}

	public void changeQ(int offset) {

		if (quantity + offset >= 0) {
			quantity += offset;
		}

	}

	public void consume(Resource other) {

		if (other != null) {
			if (this.type.equals(other.type)) {
				this.quantity += other.quantity;
			}
		}
	}

	public double getTotalPrice() {
		return unitPrice * quantity;
	}

	private Resource(Map<String, Object> props) {
		name = (String) props.get("name");
		quantity = (int) props.get("quantity");
		String typeName = (String) props.get("type");
		type = (Choices) Choices.valueOf(typeName);
		Object objUnitPrice = props.get("unitPrice");
		unitPrice = DbHelper.objectToDouble(objUnitPrice);
	}

	@JsonCreator
	public static Resource factory(Map<String, Object> props) {
		return new Resource(props);
	}

	public static class Serializer extends JsonSerializer<Resource> {

		@Override
		public void serialize(Resource value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			value.unitPrice = 0;
			jgen.writeStringField("name", value.name);
			jgen.writeNumberField("quantity", value.quantity);
			jgen.writeObjectField("type", value.type);
			jgen.writeObjectField("unitPrice", value.unitPrice);
			jgen.writeEndObject();

		}

	}
}
