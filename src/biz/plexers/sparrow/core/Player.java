package biz.plexers.sparrow.core;

public class Player {

	public Player(String username, Pirate pirate) {
		super();
		this.username = username;
		this.pirate = pirate;
	}

	public Pirate getPirate() {
		return pirate;
	}

	private String username;
	private Pirate pirate;

}
