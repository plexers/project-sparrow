package biz.plexers.sparrow.sp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class IslandManager {
	
	private List<Island> islands;
	
	public static void createIslands(){
		List<Island> islands = new ArrayList<Island>();
		
	}
	
	public static Island getIslands(){
		return Collections.unmodifiableList(islands);
	}

}
