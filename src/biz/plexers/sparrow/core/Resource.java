package biz.plexers.sparrow.core;

public class Resource {

	private String name;
	private int quantity;
	private Choices type;

	public enum Choices {

		Lumber, Cannons, Crew, Metal
	}

	public void changeQ(int offset) {

		if (quantity > offset) {
			quantity += offset;
		}

	}
}
