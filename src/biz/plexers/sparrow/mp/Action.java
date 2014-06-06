package biz.plexers.sparrow.mp;

public class Action {
	private String name;
	private int assignedCrew;
	private Choices type;

	public Action(Choices type, int assignedCrew) {
		this.assignedCrew = assignedCrew;
		this.type = type;
		this.name = type.name();
	}

	public enum Choices {
		AttackUsingCannons, LoadCannons, RepairShip
	}

	public Choices getType() {
		return type;
	}
}
