package biz.plexers.sparrow.mp;

import biz.plexers.sparrow.core.Player;

public class Battle {
	private Player player1, player2;
	private History history;

	public void submitTurnAndWaitForOpponnent(Turn turn) {

	}

	public void applyResult() {

	}

	public void addPlayer(Player p) {
		if (player1 == null) {
			player1 = p;
		} else if (player2 == null) {
			player2 = p;
		}
	}
}
