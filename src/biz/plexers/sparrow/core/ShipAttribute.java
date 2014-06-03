package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.db.Arggg;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class ShipAttribute extends Arggg{
	private String name;
	private int value;
	
	protected ShipAttribute(Map<String, Object> props) {
		name = (String) props.get("name");
		value = (int) props.get("value");
	}
	
	protected void superSerialize(JsonGenerator jgen) throws JsonGenerationException, IOException {
		jgen.writeStringField("name", name);
		jgen.writeNumberField("value", value);
	}
	
}
