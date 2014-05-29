package biz.plexers.sparrow.core;

import org.ektorp.support.CouchDbDocument;
//TODO Change serialization method
public class Pirate extends CouchDbDocument{
	
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
	private double experience;
	private ResourcesManager resourcesManager;
	private Ship ship;
	private String name;
	private double gold;
	
	

}
