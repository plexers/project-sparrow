package biz.plexers.sparrow.mp;

public class Action {
	private String name;
	private int assignedCrew;
	private Choices type;
	
	public enum Choices {
		AttackUsingCannons, LoadCannons, RepairShip
	}
}
