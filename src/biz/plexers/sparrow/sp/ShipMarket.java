package biz.plexers.sparrow.sp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.DbManager;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ShipMarket extends Market {
	List<Ship> ships;

	public ShipMarket() {
		super();
		ships = Arrays.asList(new Ship[]{new Ship(240, "Marina"), new Ship(400, "Giorgos")});
	}

	public void disassembleShip() {

	}

	public boolean buyShip(int shipIndex) throws Exception {
		Ship ship = ships.get(shipIndex);
		Pirate pirate = UserManager.getPirate();

		if (pirate.getShip() == null) {
			double dept = - ship.getGoldValue();
			if (pirate.changeGoldBy(dept)) {
				pirate.setShip(ship);
				return true;
			}
			throw new Exception("Error! Insufficient gold to buy this ship!");
		}
		throw new Exception("Error! You already have a ship!");
	}
	
	public static ShipMarket getInstance() {
		//TODO Add implementation
		return (ShipMarket) DbManager.read(ShipMarket.class, "shipMarket");
	}

	public List<Ship> getShips() {
		return Collections.unmodifiableList(ships);
	}
	
	private ShipMarket(Map<String, Object> props) {
		ships = (List<Ship>) props.get("ships");
	}
	@JsonCreator
	public static ShipMarket factory(Map<String, Object> props) {
		return new ShipMarket(props);
	}
	
}
