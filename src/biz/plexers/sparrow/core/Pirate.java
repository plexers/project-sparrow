package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import biz.plexers.sparrow.db.Arggg;
import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.sp.ResourceMarket;
import biz.plexers.sparrow.sp.ShipMarket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Pirate.Serializer.class)
public class Pirate extends Arggg {

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

	public boolean buyShip(ShipMarket shipMarket, int shipIndex)
			throws Exception {
		List<Ship> ships = shipMarket.getShips();
		Ship ship = ships.get(shipIndex);

		if (this.ship == null) {
			double dept = -ship.getGoldValue();
			if (this.changeGoldBy(dept)) {
				this.ship = ship;
				DbManager.savePirate();
				return true;
			}
			throw new Exception("Error! Insufficient gold to buy this ship!");
		}
		throw new Exception("Error! You already have a ship!");
	}

	public boolean buyResources(ResourceMarket resourceMarket) throws Exception {

		if (pay(resourceMarket.getTotalCost())) {
			this.resourcesManager.consume(resourceMarket.getResourcesManager());
			DbManager.savePirate();
			return true;
		} else {
			throw new Exception("Insufficient gold!");
		}

	}

	public boolean pay(double money) {

		return changeGoldBy(-money);
	}

	public boolean hasShip() {
		return ship != null;
	}

	public double getExperience() {
		return experience;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGold() {
		return gold;
	}

	@SuppressWarnings("unchecked")
	private Pirate(Map<String, Object> props) {
		experience = (double) props.get("experience");
		name = (String) props.get("name");
		gold = (double) props.get("gold");
		Map<String, Object> mapShip = (Map<String, Object>) props.get("ship");
		ship = DbHelper.mapAsObject(mapShip, Ship.class);
		Map<String, Object> mapRM = (Map<String, Object>) props.get("resourcesManager");
		resourcesManager = DbHelper.mapAsObject(mapRM, ResourcesManager.class);
	}

	@JsonCreator
	public static Pirate factory(Map<String, Object> props) {
		return new Pirate(props);
	}

	public static class Serializer extends JsonSerializer<Pirate> {

		@Override
		public void serialize(Pirate value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			
			jgen.writeNumberField("experience", value.experience);
			jgen.writeStringField("name", value.name);
			jgen.writeNumberField("gold", value.gold);
			jgen.writeObjectField("ship", value.ship);
			jgen.writeObjectField("resourcesManager", value.resourcesManager);
			jgen.writeEndObject();

		}

	}

}
