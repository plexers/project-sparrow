package biz.plexers.sparrow.sp;

import java.util.List;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UserManager;

public class ShipMarket extends Market {
	List<Ship> ships;

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
}
