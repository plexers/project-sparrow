package biz.plexers.sparrow.core;

import java.util.Set;

import org.ektorp.support.CouchDbDocument;

public class Ship extends CouchDbDocument{

	private Set<UpgradableShipAttribute> upgradableShipAttributes;
	private Set<InBattleShipAttribute> inBattleShipAttributes;
	private Set<StandardShipAttribute> standardShipAttributes;
	private double goldValue;
	private String name;

	public String getName() {
		return name;
	}

	public Ship(double goldValue, String name) {
		super();
		this.goldValue = goldValue;
		this.name = name;
	}

	public void applyUpgrade(Upgrade upgrade) {

	}

	public double getGoldValue() {
		return goldValue;
	}

	public void engageBattle() {

	}

}
