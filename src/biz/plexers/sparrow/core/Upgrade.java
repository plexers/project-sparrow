package biz.plexers.sparrow.core;

import java.util.HashMap;

public class Upgrade {

	private HashMap<UpgradableShipAttribute.Choices, UpgradableShipAttribute> profits;
	private ResourcesManager requirements;

	public Upgrade() {
		profits = new HashMap<>();
	}

	public void setAttribute(UpgradableShipAttribute.Choices choice, int value) {
		UpgradableShipAttribute attribute = new UpgradableShipAttribute(choice,
				value);

		profits.put(choice, attribute);
	}

}
