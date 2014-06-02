import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;
import biz.plexers.sparrow.mp.BattleManager;
import biz.plexers.sparrow.sp.ShipMarket;

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
		if (signedIn) {
			System.out.println("User " + username + " successfully loged in");
			mainWindow();
		}
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
		if (signedUp) {
			System.out.println("User " + username + " created!");
			startGame();
		}

	}

	private static void mainWindow() {
		System.out.print("1. Single Player 2. Multiplayer :");
		int choice = Integer.parseInt(s.next());

		switch (choice) {
		case 1:
			singlePlayer();
			break;
		case 2:
			multiPlayer();
			break;
		default:
			System.out.println("Wrong choice");
			break;
		}
	}

	private static void singlePlayer() {
		if (UserManager.shipExists()) {
			ShipMarket shipMarket = ShipMarket.getInstance();
			List<Ship> ships = shipMarket.getShips();
			for (int i = 0; i < ships.size(); i++) {
				Ship s = ships.get(i);
				System.out.println((i + 1) + ". " + s.getName());
			}

			int choice = Game.s.nextInt();
			try {
				Pirate pirate = UserManager.getPirate();
				pirate.buyShip(shipMarket, choice - 1);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private static void multiPlayer() {
		if (UserManager.shipExists()) {

			System.out.print("1. Create Battle 2. Join Battle :");
			int choice = Integer.parseInt(s.next());

			switch (choice) {
			case 1:
				createMpBattle();
				break;
			case 2:
				joinMpBattle();
				break;
			default:
				System.out.println("Wrong choice");
				break;
			}
			

		} else {
			System.out.println("You have to get a ship in single player.");
		}

	}

	private static void joinMpBattle() {
		ArrayList<String> openBattles = (ArrayList<String>) BattleManager
				.getBattleList();
		System.out.print("Available Battles :");

		for (int i = 0; i < openBattles.size(); i++) {
			System.out.println(i + ": " + openBattles.get(i));
		}
		System.out.print("Choose Battle :");
		int choice = Integer.parseInt(s.next());
		
		// TODO: couchdb join battle #choice

		
	}

	private static void createMpBattle() {
		// TODO Auto-generated method stub

	}
}
