package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.ektorp.support.CouchDbDocument;

import biz.plexers.sparrow.sp.ResourceMarket;
import biz.plexers.sparrow.sp.ShipMarket;

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

	public boolean buyShip(ShipMarket shipMarket, int shipIndex)
			throws Exception {
		List<Ship> ships = shipMarket.getShips();
		Ship ship = ships.get(shipIndex);

		if (this.ship == null) {
			double dept = -ship.getGoldValue();
			if (this.changeGoldBy(dept)) {
				this.ship = ship;
				return true;
			}
			throw new Exception("Error! Insufficient gold to buy this ship!");
		}
		throw new Exception("Error! You already have a ship!");
	}

	public boolean buyResources(ResourceMarket resourceMarket) throws Exception {

		if (pay(resourceMarket.getTotalCost())) {
			this.resourcesManager.consume(resourceMarket.getResourcesManager());
			return true;
		} else {
			throw new Exception("Insufficient gold!");
		}

	}

	public boolean pay(double money) {

		return changeGoldBy(-money);
	}

	public boolean hasShip() {
		return ship == null;
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
