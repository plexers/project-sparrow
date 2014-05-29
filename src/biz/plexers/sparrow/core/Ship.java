package biz.plexers.sparrow.core;
import java.util.Set;

public class Ship {
	
	private Set<UpgradableShipAttribute> upgradableShipAttributes ;
	private Set<InBattleShipAttribute> inBattleShipAttributes ;
	private Set<StandardShipAttribute> standardShipAttributes ;
	private double goldValue;
	
	
	public void applyUpgrade(Upgrade upgrade){
		
	}
	
	public double getGoldValue() {
		return goldValue;
	}

	public void engageBattle(){
		
	}

}
