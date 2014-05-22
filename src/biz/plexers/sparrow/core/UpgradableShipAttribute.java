package biz.plexers.sparrow.core;
public class UpgradableShipAttribute extends ShipAttribute {

	public enum Choice {
		Cannons, Crew, Armor, Health
	}

	private Choice type;
}
