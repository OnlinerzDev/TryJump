package at.onlinerz.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModuleOrder {
	
	public static ArrayList<Integer> moduleorder = new ArrayList<Integer>();
	
	public static void startDrawing() {
		drawModuleOrder("Easy", 3);
		drawModuleOrder("Normal", 4);
		drawModuleOrder("Hard", 2);
		drawModuleOrder("Extreme", 1);
		
	}
	
	public static void drawModuleOrder(String difficulty, Integer amount) {
		
			Map<Integer,SchematicData> map = LoadSchematics.modules;
			List<Map.Entry<Integer,SchematicData>> list = new ArrayList<Map.Entry<Integer,SchematicData>>(map.entrySet());

			Collections.shuffle(list);
			for(Map.Entry<Integer, SchematicData> e : list) { 
				if(e.getValue().getDifficulty().equalsIgnoreCase(difficulty)) {
					Integer id = e.getKey();
				
					if(! moduleorder.contains(id)) {
						if(amount > 0) {
						moduleorder.add(e.getKey());
						amount = amount-1;
						
						}
					
				}
			}
			
		}
		
		}

}
