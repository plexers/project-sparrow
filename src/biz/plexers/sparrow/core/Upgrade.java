package biz.plexers.sparrow.core;

import java.util.HashMap;

public class Upgrade {

	private HashMap<UpgradableShipAttribute.Choices, UpgradableShipAttribute> profits;
	private ResourcesManager requirements;

	public Upgrade(int cannonsValue, int crewValue, int armorValue,
			int healthValue) {

	}

	public Upgrade() {
		profits = new HashMap<>();
		requirements = new ResourcesManager();
	}

	public ResourcesManager getRequirements() {
		return requirements;
	}

	public void setAttribute(UpgradableShipAttribute.Choices choice, int value) {
		UpgradableShipAttribute attribute = new UpgradableShipAttribute(choice,
				value);

		profits.put(choice, attribute);
		requirements.calculateRequirementFor(choice, value);
	}

	public UpgradableShipAttribute getAttribute(
			UpgradableShipAttribute.Choices choice) {
		return profits.get(choice);
	}

	public void calculateRequirements() {
		// TODO Add implementation
	}

}
