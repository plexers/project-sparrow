package biz.plexers.sparrow.sp;
import biz.plexers.sparrow.core.Pirate;


public class Raid {
	
	private IslandManager islandManager;
	private Pirate pirate;
	
	public static boolean isRaidSuccesful(Island raidedIsland){
		return Math.random() <= Raid.calculateSuccessRate(raidedIsland);
	}
	
	public static double calculateSuccessRate(Island island){
		double successRate = 1 - island.getDifficulty() * 0.20;
		return successRate;
	}

}
