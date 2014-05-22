package biz.plexers.sparrow.core;
public class InBattleShipAttribute extends ShipAttribute {

	public enum Choices {
		WoundedCrew, LoadedCannons
	}

	private Choices type;
}
