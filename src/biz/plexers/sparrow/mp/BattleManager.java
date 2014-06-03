package biz.plexers.sparrow.mp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.DbManager;

public class BattleManager {

	private static Map<String, Object> openBattles;
	private static List<String> battleNames;

	public static List<String> getBattleList() {
		openBattles = DbManager.queryView("_design/multiplayer",
				"availableBattles");
		Set<String> mapBattleNames = openBattles.keySet();
		battleNames = new ArrayList<>();
		battleNames.addAll(mapBattleNames);
		return battleNames;
	}

	public static Battle choose(int battleIndex) {
		String battleName = battleNames.get(battleIndex);
		String battleID = (String) openBattles.get(battleName);
		Battle battle = DbManager.read(Battle.class, battleID);
		battle.addPlayer(UserManager.getUser());
		DbManager.save(battle);
		return battle;
	}

}
