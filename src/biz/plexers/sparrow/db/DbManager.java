package biz.plexers.sparrow.db;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import biz.plexers.sparrow.db.exceptions.SignInException;

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
}
