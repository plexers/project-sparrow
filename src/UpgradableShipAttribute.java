public class UpgradableShipAttribute extends ShipAttribute {

	private enum Choice {
		Cannons, Crew, Armor, Health
	}

	private Choice type;
}
