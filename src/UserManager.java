import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.db.exceptions.SignInException;

public class UserManager {
	private static Player user = null;

	public static boolean signIn(String username, String password)
			throws SignInException {
		if (user == null) {
			boolean signedIn = DbManager.signIn(username, password);
			if (signedIn) {
				user = new Player(username);
				return true;
			}
		}
		return false;
	}

	public static boolean signUp(String username, String password, String email) {
		// TODO Insert user to couch
		return true;
	}
}
