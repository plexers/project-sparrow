package biz.plexers.sparrow.core;

import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;

//TODO Change serialization method
public class Pirate extends CouchDbDocument {
	@JsonIgnore
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public boolean changeGoldBy(double offset) {
		if (this.gold + offset > 0) {
			this.gold += offset;
			return true;
		}
		return false;
	}

	private double experience;
	private ResourcesManager resourcesManager;
	private Ship ship;
	private String name;
	private double gold;

}
