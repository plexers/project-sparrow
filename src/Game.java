import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.core.Resource;
import biz.plexers.sparrow.core.Resource.Choices;
import biz.plexers.sparrow.core.ResourcesManager;
import biz.plexers.sparrow.core.Ship;
import biz.plexers.sparrow.core.UpgradableShipAttribute;
import biz.plexers.sparrow.core.Upgrade;
import biz.plexers.sparrow.core.UserManager;
import biz.plexers.sparrow.db.DbManager;
import biz.plexers.sparrow.db.exceptions.SignInException;
import biz.plexers.sparrow.db.exceptions.SignUpException;
import biz.plexers.sparrow.mp.Action;
import biz.plexers.sparrow.mp.Battle;
import biz.plexers.sparrow.mp.BattleManager;
import biz.plexers.sparrow.sp.Island;
import biz.plexers.sparrow.sp.IslandManager;
import biz.plexers.sparrow.sp.Raid;
import biz.plexers.sparrow.mp.Turn;
import biz.plexers.sparrow.mp.exceptions.InsufficientCrewException;
import biz.plexers.sparrow.sp.ResourceMarket;
import biz.plexers.sparrow.sp.ShipMarket;

public class Game {
	private static Scanner s;
	private static Pirate pirate;

	public static void startGame() {
		s = new Scanner(System.in);
		System.out.print("1. Login\n2. Sign-Up\nChoose: ");
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
		System.out.print("Enter username: ");
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
		System.out.print("Enter username: ");
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
		System.out.print("1. Single Player\n2. Multiplayer\nChoose: ");
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
			ShipMarket shipMarket = createShipMarket();
			try {

				chooseShip(shipMarket);
				System.out.print("Give a name for your pirate: ");
				String name = s.next();
				pirate.setName(name);
				System.out.println("Successfully created your pirate !!!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		singlePlayerChoices();
	}

	private static void chooseShip(ShipMarket shipMarket) throws Exception {
		int choice = Game.s.nextInt();
		pirate.buyShip(shipMarket, choice - 1);
	}

	private static ShipMarket createShipMarket() {
		ShipMarket shipMarket = ShipMarket.getInstance();
		List<Ship> ships = shipMarket.getShips();
		for (int i = 0; i < ships.size(); i++) {
			Ship s = ships.get(i);
			System.out.println((i + 1) + ". " + s.getName());
		}
		return shipMarket;
	}

	private static void singlePlayerChoices() {
		System.out
				.print("1. Raid Island\n2. Buy Ship\n3. Buy Resources\n4. Upgrade Ship\nChoose: ");
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

		IslandManager.createIslands();
		List<Island> islandsList = IslandManager.getIslands();
		System.out.println("Available Islands: ");
		for (int i=0; i<islandsList.size(); i++) {
			
			Map<Resource.Choices, Resource> islandResources = islandsList.get(i).getResourcesManager().getResources();
			int cannons = islandResources.get(Choices.Cannons).getQuantity();
			int crew = islandResources.get(Choices.Crew).getQuantity();
			int lumber = islandResources.get(Choices.Lumber).getQuantity();
			int metal = islandResources.get(Choices.Metal).getQuantity();
			System.out.println( (i+1) +  ". " + "Cannons:" + cannons + " Crew:" + crew + " Lumber:" + lumber + " Metal:" + metal
					+ " Success Chance: " + (int) (Raid.calculateSuccessRate(islandsList.get(i))*100) + "%");
}
		System.out.println("Select Island:");
		int choice = Integer.parseInt(s.next()) - 1;
		if (choice<=islandsList.size()){
			Island selectedIsland = islandsList.get(choice);
			if (Raid.isRaidSuccesful(selectedIsland)){
				UserManager.getPirate().takeResources(selectedIsland.getResourcesManager());
				DbManager.savePirate();
				System.out.println("Raid Succesful!");
			}
			else{
				System.out.println("Raid Unsuccesful :(");
			}
		}	
	}

	private static void buyShip() {

		if (UserManager.shipExists()) {
			System.out.print("Do you want to sell your ship? :");
			if (s.next().equalsIgnoreCase("yes")) {
				UserManager.getPirate().sellShip();
				ShipMarket shipMarket = createShipMarket();
				try {
					chooseShip(shipMarket);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

		}
		mainWindow();

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
		for (UpgradableShipAttribute.Choices choice : UpgradableShipAttribute.Choices
				.values()) {
			System.out.print(choice.name() + " : ");
			int value = s.nextInt();
			upgrade.setAttribute(choice, value);

		}
		try {
			UserManager.getShip().applyUpgrade(upgrade);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void multiPlayer() {
		if (UserManager.shipExists()) {

			System.out.print("1. Create Battle\n2. Join Battle\nChoose: ");
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
		System.out.println("Available Battles :");

		for (int i = 0; i < openBattles.size(); i++) {
			System.out.println(i + ": " + openBattles.get(i));
		}
		System.out.print("Choose Battle :");
		int choice = s.nextInt();
		try {
			Battle battle = BattleManager.choose(choice);
			pushTurn(battle);
		} catch (TimeoutException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void createMpBattle() {
		try {
			Battle battle = Battle.getInstance();
			pushTurn(battle);
		} catch (TimeoutException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void pushTurn(Battle battle) {
		Turn turn = new Turn();

		System.out.println("Enter the crew for each action: ");

		for (Action.Choices choice : Action.Choices.values()) {
			System.out.print("Enter crew for " + choice.name() + ": ");
			int crew = s.nextInt();
			Action tempAction = new Action(choice, crew);
			turn.addAction(tempAction);
		}

			try {
				pushTurn(battle.submitTurnAndWaitForOpponent(turn));
			} catch (TimeoutException e) {
				System.out.println(e.getMessage());
			} catch (InsufficientCrewException e) {
				System.out.println(e.getMessage());
				System.out.println("Try Again!");
				pushTurn(battle);
			}
	}
}
