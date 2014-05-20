
public class UserManager {
	private static Player user = null;
	
	public static void signIn(String username, String password) {
		if(user == null) {
			//TODO Password validation through couch
			user = new Player(username);
			System.out.println("User " + username + " successfully loged in!");
		}
		else 
			System.out.println("You are already loged in!");
	}
	
	public void signUp(String username, String password, String email) {
		
	}
}
