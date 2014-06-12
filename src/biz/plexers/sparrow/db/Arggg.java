package biz.plexers.sparrow.db;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class Arggg {
	protected String id;
	protected String revision;

	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("_rev")
	public void setRevision(String revision) {
		this.revision = revision;
	}

	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	protected Arggg() {
		super();
	}

	protected Arggg(Map<String, Object> props) {
		id = (String) props.get("_id");
		revision = (String) props.get("_rev");
	}

	protected void superSerialize(JsonGenerator jgen)
			throws JsonGenerationException, IOException {
		if (id != null) {
			jgen.writeStringField("_id", id);
			jgen.writeStringField("_rev", revision);
		}
	}

}
