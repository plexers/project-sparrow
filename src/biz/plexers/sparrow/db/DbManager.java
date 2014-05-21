package biz.plexers.sparrow.db;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DbManager {
	private static HttpClient client = null;
	private static CouchDbConnector db = null;
	private static String dbURL = "http://db.plexers.biz:96";
	private static String dbName = "sol";

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
				throw new SignInException("Invalid Username or Password!");
			}
			return true;
		}
		throw new SignInException("You are already signed in!");
	}

	public static boolean signUp(String username, String password, String email)
			throws SignUpException {
		HttpClient client = null;
		try {
			client = new StdHttpClient.Builder().url(dbURL).build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		CouchDbConnector usersDb = dbInstance.createConnector("_users", false);
		try {
			usersDb.create(getUser(username, password, email));
		} catch (Exception e) {
			throw new SignUpException("Username not Available");
		}
		return false;
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

}
