package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class ShipAttribute<Choices> {
	protected String name;
	private int value;

	public ShipAttribute(int value) {
		this.value = value;
	}

	public void consume(ShipAttribute other) {
		this.value += other.value;

	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void changeValueBy(double offset){
		this.value+= offset;
	}

	protected ShipAttribute(Map<String, Object> props) {
		name = (String) props.get("name");
		value = (int) props.get("value");
	}

	protected void superSerialize(JsonGenerator jgen)
			throws JsonGenerationException, IOException {
		jgen.writeStringField("name", name);
		jgen.writeNumberField("value", value);
	}

	public abstract Choices getType();

}
