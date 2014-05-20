import java.util.Scanner;
import java.util.regex.Pattern;

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
		UserManager.signIn(username, password);
	}

	private static void signUp() {

	}
}
