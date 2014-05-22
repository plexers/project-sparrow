import java.util.Scanner;
import java.util.regex.Pattern;

import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;

public class Game {
	private static Scanner s;

	public static void startGame() {
		s = new Scanner(System.in);
		System.out.print("1. Login 2. Sign-Up :");
		int choice = Integer.parseInt(s.next(Pattern.compile("\\A[12]\\Z")));

		switch (choice) {
		case 1:
			login();
			break;
		case 2:
			signUp();
			break;
		default:
			System.out.println("Wrong choice");
			break;
		}

	}

	public static void main(String[] args) {
		startGame();
	}

	private static void login() {
		System.out.print("Enter username:");
		String username = s.next();
		System.out.print("Enter password: ");
		String password = s.next();
		boolean signedIn = false;
		try {
			signedIn = UserManager.signIn(username, password);
		} catch (SignInException e) {
			System.out.println(e.getMessage());
		}
		if(signedIn)
			System.out.println("User " + username + " successfully loged in");
	}

	private static void signUp() {
		System.out.print("Enter username:");
		String username = s.next();
		System.out.print("Enter password: ");
		String password = s.next();
		System.out.print("Enter email: ");
		String email = s.next();
		boolean signedUp = false;
		try {
			signedUp = UserManager.signUp(username, password, email);
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
		}
		if(signedUp) {
			System.out.println("User " + username + " created!");
			startGame();
		}
			

	}
}
