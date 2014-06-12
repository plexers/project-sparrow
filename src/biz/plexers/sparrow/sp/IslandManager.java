package biz.plexers.sparrow.sp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class IslandManager {
	
	private static List<Island> islands;
	
	public static void createIslands(){
		islands = new ArrayList<Island>();
		for(int difficulty=0; difficulty < 3; difficulty++){
			islands.add(difficulty, new Island(difficulty));
		}
				
	}
	
	public static List<Island> getIslands(){
		return Collections.unmodifiableList(islands);
	}

}
