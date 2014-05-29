package biz.plexers.sparrow.core;

import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;

public class UserManager {
	private static Player user = null;

	public static boolean signIn(String username, String password)
			throws SignInException {
		if (user == null) {
			boolean signedIn = DbManager.signIn(username, password);
			if (signedIn) {
				Pirate pirate = DbManager.readPirate();
				// TODO Remove this testing code line
				System.out.println(pirate.getExperience() + " "
						+ pirate.getGold());
				user = new Player(username, pirate);
				return true;
			}
		}
		return false;
	}

	public static boolean signUp(String username, String password, String email)
			throws SignUpException {
		return DbManager.signUp(username, password, email);

	}

	public static boolean isFirstTIme() {
		return user.getPirate().getShip() == null;
	}

	public static Pirate getPirate() {
		return user.getPirate();
	}
}
