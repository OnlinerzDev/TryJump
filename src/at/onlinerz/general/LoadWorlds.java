package at.onlinerz.general;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class LoadWorlds {
	
	public static void searchWorlds() {
		
		String[] worlds = new String[]{"world", "world_nether", "world_the_end", "jumpworld"};
		List<String> defaultWorlds = Arrays.asList(worlds);
		
		for(File f : Bukkit.getServer().getWorldContainer().listFiles()) {
			if(f.isDirectory()) {
				File check = new File(f.getPath()+"/playerdata");
				File check2 = new File(f.getPath()+"/level.dat");
				if(check.exists() && check2.exists()) {
					if(! defaultWorlds.contains(f.getName())) {
						new WorldCreator(f.getName()).createWorld();
						System.out.println("Loaded world "+f.getName());
					}
					
						}
					}
				}
				
	}


}
