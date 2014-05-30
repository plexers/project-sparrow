package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Pirate.Serializer.class)
public class Pirate extends CouchDbDocument {

	private double experience;
	private ResourcesManager resourcesManager;
	private Ship ship;
	private String name;
	private double gold;

	public boolean changeGoldBy(double offset) {
		if (this.gold + offset > 0) {
			this.gold += offset;
			return true;
		}
		return false;
	}

	private Pirate(Map<String, Object> props) {
		experience = (double) props.get("experience");
		name = (String) props.get("name");
		gold = (double) props.get("gold");
		// TODO Uncomment these lines when deserializers ready
		// ship = (Ship) props.get("ship");
		// resourcesManager = (ResourcesManager) props.get("resourcesManager");
	}

	@JsonCreator
	public static Pirate factory(Map<String, Object> props) {
		return new Pirate(props);
	}

	public class Serializer extends JsonSerializer<Pirate> {

		@Override
		public void serialize(Pirate value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			jgen.writeNumberField("experience", value.experience);
			jgen.writeStringField("name", value.name);
			jgen.writeNumberField("gold", value.gold);
			// TODO Uncomment these lines when serializers ready
			// jgen.writeObjectField("ship", value.ship);
			// jgen.writeObjectField("resourceManager", value.resourcesManager);
			jgen.writeEndObject();

		}

	}

}
