package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ResourcesManager.Serializer.class)
public class ResourcesManager {

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

	private ResourcesManager(Map<String, Object> props) {
		resources = (HashMap<Resource.Choices, Resource>) props
				.get("resources");
	}

	@JsonCreator
	public static ResourcesManager factory(Map<String, Object> props) {
		return new ResourcesManager(props);
	}

	public class Serializer extends JsonSerializer<ResourcesManager> {

		@Override
		public void serialize(ResourcesManager value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			jgen.writeObjectField("resources", resources);
			jgen.writeEndObject();

		}

	}

}
