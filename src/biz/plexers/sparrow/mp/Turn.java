package biz.plexers.sparrow.mp;

import java.util.HashMap;
import java.util.Map;

public class Turn {
	Map<Action.Choices, Action> actions;

	public Turn() {
		actions = new HashMap();
	}

	public void addAction(Action a) {
		actions.put(a.getType(), a);
	}
}
