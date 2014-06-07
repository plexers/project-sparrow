package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biz.plexers.sparrow.core.UpgradableShipAttribute.Choices;
import biz.plexers.sparrow.db.Arggg;
import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.mp.Turn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

	public void applyUpgrade(Upgrade upgrade) throws Exception {
		if (!UserManager.getPirate().pay(upgrade)) {
			throw new Exception("Insufficient resources for this upgrade!");
		}
		for (UpgradableShipAttribute attribute : upgradableShipAttributes
				.values()) {
			Choices choice = attribute.getType();
			UpgradableShipAttribute otherAttribute = upgrade
					.getAttribute(choice);
			attribute.consume(otherAttribute);
		}

		DbManager.savePirate();

	}

	public boolean hasEnoughCrewFor(Turn t) {
		int crewRequirements = t.getCrewRequirements();
		UpgradableShipAttribute.Choices upgradableType = UpgradableShipAttribute.Choices.Crew;
		UpgradableShipAttribute crewOnBoard = upgradableShipAttributes
				.get(upgradableType);
		InBattleShipAttribute.Choices inbattleType = InBattleShipAttribute.Choices.WoundedCrew;
		InBattleShipAttribute woundedCrew = inBattleShipAttributes
				.get(inbattleType);
		int availableCrew = crewOnBoard.getValue() - woundedCrew.getValue();
		return crewRequirements <= availableCrew;
	}

	public double getGoldValue() {
		return goldValue;
	}

	public void engageBattle() {
		for(InBattleShipAttribute a: inBattleShipAttributes.values()) {
			a.setValue(0);
		}
	}

	public double getPresentValue() {
		// TODO Improve Calculations
		return goldValue;
	}

	@SuppressWarnings("unchecked")
	private Ship(Map<String, Object> props) {
		goldValue = (double) props.get("goldValue");
		name = (String) props.get("name");
		Object upgradableMap = props.get("upgradableShipAttributes");
		upgradableShipAttributes = DbHelper
				.mapAsObject(
						upgradableMap,
						new TypeReference<HashMap<UpgradableShipAttribute.Choices, UpgradableShipAttribute>>() {
						});
		Object inBattleMap = props.get("inBattleShipAttributes");
		inBattleShipAttributes = DbHelper
				.mapAsObject(
						inBattleMap,
						new TypeReference<HashMap<InBattleShipAttribute.Choices, InBattleShipAttribute>>() {
						});
		standardShipAttributes = DbHelper
				.mapAsObject(
						props.get("standardShipAttributes"),
						new TypeReference<HashMap<StandardShipAttribute.Choices, StandardShipAttribute>>() {
						});
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
