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

}
