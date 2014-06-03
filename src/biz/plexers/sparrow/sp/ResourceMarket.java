package biz.plexers.sparrow.sp;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import biz.plexers.sparrow.core.Resource;
import biz.plexers.sparrow.core.Resource.Choices;
import biz.plexers.sparrow.core.ResourcesManager;
import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.db.DbManager;

public class ResourceMarket extends Market {

	private ResourcesManager resourcesManager;

	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public static ResourceMarket getInstance() {
		return (ResourceMarket) DbManager.read(ResourceMarket.class, "resourceMarket");
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
	
	@SuppressWarnings("unchecked")
	private ResourceMarket(Map<String, Object> props) {
		id = (String) props.get("_id");
		revision = (String) props.get("_rev");
		Map<String, Object> mapRM = (Map<String, Object>) props.get("resourcesManager");
		resourcesManager = DbHelper.mapAsObject(mapRM, ResourcesManager.class);
	}
	
	@JsonCreator
	public static ResourceMarket factory (Map<String, Object> props) {
		return new ResourceMarket(props);
	}

}
