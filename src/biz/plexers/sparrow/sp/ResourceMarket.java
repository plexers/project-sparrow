package biz.plexers.sparrow.sp;

import java.util.Map;

import biz.plexers.sparrow.core.Resource;
import biz.plexers.sparrow.core.Resource.Choices;
import biz.plexers.sparrow.core.ResourcesManager;

public class ResourceMarket extends Market {

	private ResourcesManager resourcesManager;

	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public static ResourceMarket getInstance() {
		// TODO Add Implementation
		return null;
	}

	public double getTotalCost() {

		Map<Choices, Resource> resources = resourcesManager.getResources();
		double sum = 0;
		for (Choices choice : Resource.Choices.values()) {

			Resource resource = resources.get(choice);
			if (resource != null) {
				double cost = resource.getTotalPrice();
				sum += cost;
			}
		}
		return sum;
	}

}
