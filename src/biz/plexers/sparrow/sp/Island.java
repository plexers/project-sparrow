package biz.plexers.sparrow.sp;
import biz.plexers.sparrow.core.ResourcesManager;


public class Island {

	private ResourcesManager resourcesManager;
	private int difficulty;
	
	public Island(int difficulty) {
		this.difficulty = difficulty;
		resourcesManager= ResourcesManager.islandResources(difficulty);
	}

	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public int getDifficulty() {
		return difficulty;
	}
	
	
}
