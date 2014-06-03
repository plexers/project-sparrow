package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biz.plexers.sparrow.db.Arggg;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Ship.Serializer.class)
public class Ship extends Arggg {

	private HashMap<UpgradableShipAttribute.Choices, UpgradableShipAttribute> upgradableShipAttributes;
	private HashMap<InBattleShipAttribute.Choices, InBattleShipAttribute> inBattleShipAttributes;
	private HashMap<StandardShipAttribute.Choices, StandardShipAttribute> standardShipAttributes;
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

	@SuppressWarnings("unchecked")
	private Ship(Map<String, Object> props) {
		goldValue = (double) props.get("goldValue");
		name = (String) props.get("name");
		upgradableShipAttributes = (HashMap<UpgradableShipAttribute.Choices,UpgradableShipAttribute>) props
				.get("upgradableShipAttributes");
		inBattleShipAttributes = (HashMap<InBattleShipAttribute.Choices,InBattleShipAttribute>) props
				.get("inBattleShipAttributes");
		standardShipAttributes = (HashMap<StandardShipAttribute.Choices,StandardShipAttribute>) props
				.get("standardShipAttributes");
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
			jgen.writeNumberField("goldValue", value.goldValue);
			jgen.writeStringField("name", value.name);
			jgen.writeObjectField("inBattleShipAttributes",
					value.inBattleShipAttributes);
			jgen.writeObjectField("upgradableShipAttributes",
					value.upgradableShipAttributes);
			jgen.writeObjectField("standardShipAttributes",
					value.standardShipAttributes);
			jgen.writeEndObject();

		}

	}

}
