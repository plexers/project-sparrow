package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import biz.plexers.sparrow.core.Resource.Choices;
import biz.plexers.sparrow.db.DbHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ResourcesManager.Serializer.class)
public class ResourcesManager {

	public ResourcesManager() {
		resources = new HashMap<>();
		resources.put(Choices.Cannons, new Resource(Choices.Cannons));
		resources.put(Choices.Crew, new Resource(Choices.Crew));
		resources.put(Choices.Lumber, new Resource(Choices.Lumber));
		resources.put(Choices.Metal, new Resource(Choices.Metal));
	}

	private HashMap<Resource.Choices, Resource> resources;

	public Map<Resource.Choices, Resource> getResources() {
		return Collections.unmodifiableMap(resources);
	}

	public void changeResourceBy(Resource.Choices resourceType, int offset) {

		Resource tempRes = resources.get(resourceType);

		if (tempRes != null) {
			tempRes.changeQ(offset);
		}
	}

	public void consume(ResourcesManager other) {
		
		HashMap<Choices, Resource> otherResources = other.resources;
		for (Resource.Choices choice : Resource.Choices.values()) {
			Resource thisTempRes = resources.get(choice);
			Resource otherTempRes = otherResources.get(choice);

			if (thisTempRes != null) {
				thisTempRes.consume(otherTempRes);
			} else {
				resources.put(choice, otherTempRes);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private ResourcesManager(Map<String, Object> props) {
		Map<String, Object> mapResources = (Map<String, Object>) props
				.get("resources");
		resources = DbHelper.mapAsObject(mapResources,
				new TypeReference<HashMap<Resource.Choices, Resource>>() {
				});
	}

	@JsonCreator
	public static ResourcesManager factory(Map<String, Object> props) {
		return new ResourcesManager(props);
	}

	public static class Serializer extends JsonSerializer<ResourcesManager> {

		@Override
		public void serialize(ResourcesManager value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			jgen.writeObjectField("resources", value.resources);
			jgen.writeEndObject();

		}

	}

}
