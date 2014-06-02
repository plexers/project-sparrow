package biz.plexers.sparrow.db;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}
