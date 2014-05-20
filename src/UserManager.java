public class UserManager {
	private static Player user = null;

	public static boolean signIn(String username, String password) {
		if (user == null) {
			// TODO Password validation through couch
			user = new Player(username);
			return true;
		} 
		return false;
	}

	public static boolean signUp(String username, String password, String email) {
		//TODO Insert user to couch
		return true;
	}
}
