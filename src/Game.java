import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Resource;
import biz.plexers.sparrow.core.ResourcesManager;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UpgradableShipAttribute;
import biz.plexers.sparrow.core.Upgrade;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;
import biz.plexers.sparrow.mp.Battle;
import biz.plexers.sparrow.mp.BattleManager;
import biz.plexers.sparrow.sp.ResourceMarket;
import biz.plexers.sparrow.sp.ShipMarket;

public class Game {
	private static Scanner s;
	private static Pirate pirate;

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
			pirate = UserManager.getPirate();
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
		if (!UserManager.shipExists()) {
			ShipMarket shipMarket = ShipMarket.getInstance();
			List<Ship> ships = shipMarket.getShips();
			for (int i = 0; i < ships.size(); i++) {
				Ship s = ships.get(i);
				System.out.println((i + 1) + ". " + s.getName());
			}

			int choice = Game.s.nextInt();
			try {
				pirate.buyShip(shipMarket, choice - 1);
				System.out.print("Give a name for your pirate: ");
				String name = s.next();
				pirate.setName(name);
				System.out.println("Successfully created your pirate !!!");
				singlePlayerChoices();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private static void singlePlayerChoices() {
		System.out
				.print("1. Raid Island 2. Buy Ship  3. Buy Resources 4. Upgrade Ship :");
		int choice = Integer.parseInt(s.next());

		switch (choice) {
		case 1:
			raidIsland();
			break;
		case 2:
			buyShip();
			break;
		case 3:
			buyResources();
			break;
		case 4:
			upgradeShip();
			break;
		default:
			System.out.println("Wrong choice");
			break;
		}
	}

	private static void raidIsland() {

	}

	private static void buyShip() {

	}

	private static void buyResources() {

		ResourceMarket resourceMarket = ResourceMarket.getInstance();
		ResourcesManager marketResourcesManager = resourceMarket
				.getResourcesManager();

		System.out.println("Enter quantity for each resource :");
		for (Resource.Choices choice : Resource.Choices.values()) {
			System.out.print(choice + " :");
			int q = s.nextInt();
			marketResourcesManager.changeResourceBy(choice, q);
		}
		try {
			pirate.buyResources(resourceMarket);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		singlePlayerChoices();

	}

	private static void upgradeShip() {
		System.out.println("Enter upgrade values: ");
		Upgrade upgrade = new Upgrade();
		for(UpgradableShipAttribute.Choices choice : UpgradableShipAttribute.Choices.values()) {
			System.out.print(choice.name() +" : ");
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
		List<String> openBattles = BattleManager.getBattleList();
		System.out.print("Available Battles :");

		for (int i = 0; i < openBattles.size(); i++) {
			System.out.println(i + ": " + openBattles.get(i));
		}
		System.out.print("Choose Battle :");
		int choice = s.nextInt();
		Battle battle = BattleManager.choose(choice);

	}

	private static void createMpBattle() {
		try {
			Battle battle = Battle.getInstance();
		} catch (TimeoutException e) {
			System.out.println(e.getMessage());
		}
	}
}
