package biz.plexers.sparrow.core;

public class StandardShipAttribute extends ShipAttribute{
	private Choices type;
	
	public enum Choices {
		MaxCannons, MaxCrew, MaxHealth
	}
}
