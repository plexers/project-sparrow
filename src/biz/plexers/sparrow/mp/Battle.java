package biz.plexers.sparrow.mp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import biz.plexers.sparrow.core.InBattleShipAttribute;
import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Player;
import biz.plexers.sparrow.core.ResourcesManager;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UpgradableShipAttribute;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.Arggg;
import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.mp.Action.Choices;
import biz.plexers.sparrow.mp.exceptions.InsufficientCrewException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Battle.Serializer.class)
public class Battle extends Arggg {
	private Player player1, player2;
	private History history;

	private Battle() {
		history = new History();
		player1 = UserManager.getUser();
		UserManager.getShip().engageBattle();
	}

	public static Battle getInstance() throws TimeoutException {
		Battle res = new Battle();
		DbManager.save(res);
		try {
			return DbManager.waitForChanges(res, Battle.class, 30,
					TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			DbManager.delete(res);
			throw new TimeoutException("No player joined the game!");
		}
	}

	public Battle submitTurnAndWaitForOpponent(Turn turn)
			throws TimeoutException, InsufficientCrewException {
		applyResult(turn);
		history.pushTurn(turn);
		Battle newBattle = submitAndWait();
		return newBattle;

	}

	public Battle submitAndWait() throws TimeoutException {
		DbManager.save(this);
		Battle newBattle;
		try {
			newBattle = DbManager.waitForChanges(this, Battle.class, 30,
					TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			throw new TimeoutException("Your opponent did not respond in time!");
		}

		return newBattle;
	}

	public void applyResult(Turn turn) {
		Ship myShip;
		Ship enemyShip;
		Pirate myPirate;
		if (this.history.amIplayer1()){
			myShip = this.player1.getPirate().getShip();
			enemyShip = this.player2.getPirate().getShip();
			myPirate = player1.getPirate();
		}
		else{
			myShip = this.player2.getPirate().getShip();
			enemyShip = this.player1.getPirate().getShip();
			myPirate = player2.getPirate();
		}
		
		int attackCrew = turn.actions.get(Choices.AttackUsingCannons).getAssignedCrew();
		applyAttackAction(myShip, enemyShip, attackCrew);
		int repairCrew = turn.actions.get(Choices.RepairShip).getAssignedCrew();
		applyRepairAction(myShip, myPirate, repairCrew);
		int LoadCrew = turn.actions.get(Choices.LoadCannons).getAssignedCrew();
		applyLoadCannonsAction(myShip, LoadCrew);
		
				
	}
	
	private void applyLoadCannonsAction(Ship myShip, int loadCrew) {
		int shipCannons =  (int)myShip.getUpgradableShipAttributeValue(UpgradableShipAttribute.Choices.Cannons);
		int loadedCannons = (int)myShip.getInBattleShipAttributeValue(InBattleShipAttribute.Choices.LoadedCannons);
		int newlyLoaded = (int) Math.min(loadCrew, shipCannons - loadedCannons);
		myShip.changeInBattleShipAttributeBy(InBattleShipAttribute.Choices.LoadedCannons, newlyLoaded);
	}

	private void applyRepairAction(Ship myShip, Pirate myPirate, int repairCrew) {
		int repairAmount;
		int availableWood = myPirate.getLumber();
		int damageTaken = (int) myShip.getInBattleShipAttributeValue(InBattleShipAttribute.Choices.DamageTaken);
		repairAmount = Math.min(repairCrew*3, availableWood);
		if (repairAmount > damageTaken) repairAmount = damageTaken;
		if (myPirate.useLumberQuantity(repairAmount))
			myShip.changeInBattleShipAttributeBy(InBattleShipAttribute.Choices.DamageTaken, -repairAmount);
	}

	private void applyAttackAction(Ship myShip, Ship enemyShip, int attackCrew){
		double firedCannons = Math.min(attackCrew, myShip.getInBattleShipAttributeValue(InBattleShipAttribute.Choices.LoadedCannons));
		myShip.changeInBattleShipAttributeBy(InBattleShipAttribute.Choices.LoadedCannons, -firedCannons);
		double enemyArmor= enemyShip.getUpgradableShipAttributeValue(UpgradableShipAttribute.Choices.Armor);
		int damageTaken= (int) Math.floor(firedCannons*(100/(100+enemyArmor)*(1.1-Math.random()*0.2)));
		enemyShip.changeInBattleShipAttributeBy(InBattleShipAttribute.Choices.DamageTaken, damageTaken);
		
		double crewWoundChance = damageTaken / enemyShip.getUpgradableShipAttributeValue(UpgradableShipAttribute.Choices.Health);
		int enemyCrew = (int) Math.floor(enemyShip.getUpgradableShipAttributeValue(UpgradableShipAttribute.Choices.Crew));
		int enemyWoundedCrew =  (int) Math.floor(enemyShip.getInBattleShipAttributeValue(InBattleShipAttribute.Choices.WoundedCrew));
		int newlyWounded= 0;
		for(int i=enemyWoundedCrew; i<= enemyCrew; i++){
			if (Math.random()<= crewWoundChance) newlyWounded++;
		}
		enemyShip.changeInBattleShipAttributeBy(InBattleShipAttribute.Choices.WoundedCrew, newlyWounded);		
	}

	public void addPlayer() {
		UserManager.getShip().engageBattle();
		Player p = UserManager.getUser();
		if (player1 == null) {
			player1 = p;
		} else if (player2 == null) {
			player2 = p;
		}
	}

	private Battle(Map<String, Object> props) {
		super(props);
		Object objHistory = props.get("history");
		history = DbHelper.mapAsObject(objHistory, History.class);
		Object objPlayer1 = props.get("player1");
		player1 = DbHelper.mapAsObject(objPlayer1, Player.class);
		Object objPlayer2 = props.get("player2");
		player2 = DbHelper.mapAsObject(objPlayer2, Player.class);
	}

	@JsonCreator
	public static Battle factory(Map<String, Object> props) {
		return new Battle(props);
	}

	public static class Serializer extends JsonSerializer<Battle> {

		@Override
		public void serialize(Battle value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();

			value.superSerialize(jgen);
			jgen.writeObjectField("history", value.history);
			jgen.writeObjectField("player1", value.player1);
			jgen.writeObjectField("player2", value.player2);

			jgen.writeEndObject();

		}

	}
}
