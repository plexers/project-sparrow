package biz.plexers.sparrow.sp;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.db.DbManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShipMarket extends Market {
	List<Ship> ships;

	public void disassembleShip() {

	}

	public static ShipMarket getInstance() {
		return (ShipMarket) DbManager.read(ShipMarket.class, "shipMarket");
	}

	public List<Ship> getShips() {
		return Collections.unmodifiableList(ships);
	}

	private ShipMarket(Map<String, Object> props) {
		id = (String) props.get("_id");
		revision = (String) props.get("_rev");
		ObjectMapper objectMapper = new ObjectMapper();
		Object rawShips = props.get("ships");
		try {
			String jsonString = objectMapper.writeValueAsString(rawShips);
			ships = objectMapper.readValue(jsonString, new TypeReference<List<Ship>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@JsonCreator
	public static ShipMarket factory(Map<String, Object> props) {
		return new ShipMarket(props);
	}

}
