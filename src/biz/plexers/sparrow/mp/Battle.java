package biz.plexers.sparrow.mp;

import java.io.IOException;
import java.util.Map;

import biz.plexers.sparrow.core.Player;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.Arggg;
import biz.plexers.sparrow.db.DbHelper;
import biz.plexers.sparrow.db.DbManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Battle.Serializer.class)
public class Battle extends Arggg{
	private Player player1, player2;
	private History history;
	
	private Battle() {
		history = new History();
		player1 = UserManager.getUser();
	}
	
	public static Battle getInstance() {
		Battle res = new Battle();
		DbManager.save(res);
		return res; 
	}

	public void submitTurnAndWaitForOpponnent(Turn turn) {

	}

	public void applyResult() {

	}

	public void addPlayer(Player p) {
		if (player1 == null) {
			player1 = p;
		} else if (player2 == null) {
			player2 = p;
		}
	}
	
	private Battle(Map<String, Object> props) {
		super(props);
		Object objHistory = props.get("history");
		history = DbHelper.mapAsObject(objHistory, History.class);
		Object objPlayer1 = props.get("player1");
		player1 = DbHelper.mapAsObject(objPlayer1, Player.class);
		Object objPlayer2 = props.get("player2");
		player2 = DbHelper.mapAsObject(objPlayer2, Player.class);
	}
	
	@JsonCreator
	public static Battle factory(Map<String, Object> props) {
		return new Battle(props);
	}
	

	public static class Serializer extends JsonSerializer<Battle> {

		@Override
		public void serialize(Battle value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartObject();
			
			value.superSerialize(jgen);
			jgen.writeObjectField("history", value.history);
			jgen.writeObjectField("player1", value.player1);
			jgen.writeObjectField("player2", value.player2);
			
			jgen.writeEndObject();

		}

	}
}
