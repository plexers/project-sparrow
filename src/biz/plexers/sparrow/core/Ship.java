package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Ship.Serializer.class)
public class Ship extends CouchDbDocument {

	private Set<UpgradableShipAttribute> upgradableShipAttributes;
	private Set<InBattleShipAttribute> inBattleShipAttributes;
	private Set<StandardShipAttribute> standardShipAttributes;
	private double goldValue;
	private String name;

	public String getName() {
		return name;
	}

	public void applyUpgrade(Upgrade upgrade) {

	}

	public double getGoldValue() {
		return goldValue;
	}

	public void engageBattle() {

	}

	private Ship(Map<String, Object> props) {
		super();
		goldValue = (double) props.get("goldValue");
		name = (String) props.get("name");
		// TODO Uncomment these lines where deserializers ready
		// upgradableShipAttributes = (Set<UpgradableShipAttribute>)
		// props.get("upgradableShipAttributes");
		// inBattleShipAttributes = (Set<InBattleShipAttribute>)
		// props.get("inBattleShipAttributes");
		// standardShipAttributes = (Set<StandardShipAttribute>)
		// props.get("standardShipAttributes");
	}

	@JsonCreator
	public static Ship factory(Map<String, Object> props) {
		return new Ship(props);
	}

	public static class Serializer extends JsonSerializer<Ship> {

		@Override
		public void serialize(Ship value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			jgen.writeNumberField("goldVaule", value.goldValue);
			jgen.writeStringField("name", value.name);
			// TODO Uncomment these lines where serializers ready
			// jgen.writeObjectField("inBattleShipAttributes",
			// value.inBattleShipAttributes);
			// jgen.writeObjectField("upgradableShipAttributes",
			// value.upgradableShipAttributes);
			// jgen.writeObjectField("standardShipAttributes",
			// value.standardShipAttributes);
			jgen.writeEndObject();

		}

	}

}
