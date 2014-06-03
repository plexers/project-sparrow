package biz.plexers.sparrow.core;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.db.DbHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Player.Serializer.class)
public class Player {

	public Player(String username, Pirate pirate) {
		super();
		this.username = username;
		this.pirate = pirate;
	}

	public Pirate getPirate() {
		return pirate;
	}

	private String username;
	private Pirate pirate;
	
	private Player(Map<String, Object> props) {
		username = (String) props.get("username");
		Object objPirate = props.get("pirate");
		pirate = DbHelper.mapAsObject(objPirate, Pirate.class);
	}

	@JsonCreator
	public static Player factory(Map<String, Object> props) {
		return new Player(props);
	}

	public static class Serializer extends JsonSerializer<Player> {

		@Override
		public void serialize(Player value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			
			jgen.writeStringField("username", value.username);
			jgen.writeObjectField("pirate", value.pirate);
			
			jgen.writeEndObject();

		}

	}

}
