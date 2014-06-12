package biz.plexers.sparrow.sp;
import java.util.Map;

import biz.plexers.sparrow.core.Pirate;
import biz.plexers.sparrow.db.Arggg;


public abstract class Market extends Arggg{
	
	protected Market() {
		super();
	}
	
	protected Market(Map<String, Object> props) {
		super(props);
	}

	private Pirate pirate;

	
}
