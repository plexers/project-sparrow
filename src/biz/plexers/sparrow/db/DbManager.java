package biz.plexers.sparrow.db;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DbManager {
	private static HttpClient client = null;
	private static CouchDbConnector db = null;
	private static String dbURL = "http://db.plexers.biz:96";
	private static String dbName = "sol";
	private static String username = null;

	public static boolean signIn(String username, String password)
			throws SignInException {
		if (client == null) {
			try {
				client = new StdHttpClient.Builder().url(dbURL)
						.username(username).password(password).build();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			CouchDbInstance dbInstance = new StdCouchDbInstance(client);
			try {
				db = dbInstance.createConnector(dbName, true);
			} catch (DbAccessException e) {
				client = null;
				throw new SignInException("Invalid Username or Password!");
			}
			DbManager.username = username;
			return true;
		}
		throw new SignInException("You are already signed in!");
	}

	public static boolean signUp(String username, String password, String email)
			throws SignUpException {
		CouchDbConnector usersDb = getUsersDb();
		// TODO Find a way to create a pirate
		try {
			usersDb.create(getUser(username, password, email));
		} catch (Exception e) {
			throw new SignUpException("Username not Available");
		}
		return false;
	}

	private static CouchDbConnector getUsersDb() {
		HttpClient client = getClient();
		CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		CouchDbConnector usersDb = dbInstance.createConnector("_users", false);
		return usersDb;
	}

	private static HttpClient getClient() {
		HttpClient client = null;
		if (DbManager.client != null)
			return DbManager.client;
		try {
			client = new StdHttpClient.Builder().url(dbURL).build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return client;
	}

	private static JsonNode getUser(String username, String password,
			String email) {
		ObjectNode user = JsonNodeFactory.instance.objectNode();
		user.put("_id", "org.couchdb.user:" + username);
		user.put("name", username);
		user.put("type", "user");
		user.put("roles", JsonNodeFactory.instance.arrayNode());
		user.put("password", password);
		user.put("email", email);
		return user;
	}

	private static JsonNode getUser(String username) {
		CouchDbConnector usersDb = getUsersDb();

		JsonNode jsonUser = usersDb.get(JsonNode.class, "org.couchdb.user:"
				+ username);

		return jsonUser;
	}

	public static Pirate readPirate() {
		JsonNode jsonUser = getUser(username);

		JsonNode jsonPirate = jsonUser.findPath("pirate");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonPirate.toString(), Pirate.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void savePirate() {
		JsonNode jsonUser = getUser(username);

		if (jsonUser.isObject()) {
			ObjectMapper objectMapper = new ObjectMapper();
			Pirate pirate = UserManager.getPirate();
			JsonNode jsonPirate = objectMapper.valueToTree(pirate);

			ObjectNode objectUser = (ObjectNode) jsonUser;
			objectUser.put("pirate", jsonPirate);
			getUsersDb().update(objectUser);
		}
	}

	public static void save(Object o) {
		if (o instanceof Arggg) {
			if (!isDbDoc(o))
				db.create(o);
			else
				db.update(o);
		}
	}

	public static <E> E read(Class<E> clz, String id) {
		return (E) db.get(clz, id);
	}

	private static boolean isDbDoc(Object o) {
		if (o instanceof Arggg) {
			Arggg arggg = (Arggg) o;
			String id = arggg.getId();
			return !(id == null || id.length() == 0);
		}
		return false;
	}

	public static <T> T waitForChanges(Object o, Class<T> clz, long timeout,
			TimeUnit timeUnit) throws TimeoutException {
		if (isDbDoc(o)) {
			Arggg arggg = (Arggg) o;
			ChangesCommand cmd = new ChangesCommand.Builder()
					.filter("multiplayer/battleChanges")
					.param("id", arggg.getId()).build();

			ChangesFeed feed = db.changesFeed(cmd);
			while (feed.isAlive()) {
				try {
					DocumentChange change = feed.next(timeout, timeUnit);
					if (change == null)
						throw new TimeoutException();
					String docId = change.getId();
					if (docId.equals(arggg.getId())) {
						feed.cancel();
						return (T) db.get(clz, arggg.getId());
					}
					System.out.println(docId);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	public static Map<String, Object> queryView(String desingDocId,
			String viewName) {
		ViewQuery query = new ViewQuery().designDocId(desingDocId).viewName(
				viewName);
		ViewResult result = db.queryView(query);
		Map<String, Object> map = new HashMap<String, Object>();
		for (ViewResult.Row row : result) {
			map.put(row.getKey(), row.getValue());
		}
		return map;
	}

}
