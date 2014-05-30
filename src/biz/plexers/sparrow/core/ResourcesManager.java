package biz.plexers.sparrow.core;

import java.util.HashMap;

public class ResourcesManager {

	private HashMap<Resource.Choices, Resource> resources;

	public void changeResourceBy(Resource.Choices resourceType, int offset) {

		Resource tempRes = resources.get(resourceType);

		if (tempRes != null) {
			tempRes.changeQ(offset);
		}
	}

	public void consume(ResourcesManager other) {

		for (Resource.Choices choice : Resource.Choices.values()) {
			Resource thisTempRes = resources.get(choice);
			Resource otherTempRes = resources.get(choice);

			if (thisTempRes != null) {
				thisTempRes.consume(otherTempRes);
			} else {
				resources.put(choice, otherTempRes);
			}
		}
	}

}
