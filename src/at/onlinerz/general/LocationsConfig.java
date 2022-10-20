package at.onlinerz.general;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class LocationsConfig {
	
	public static File file = new File("plugins/TryJump/Locations/lobbies.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static File file2 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	public static void Creation() {

		if(! file.exists()) {
			Location wartelobbyloc = new Location(Bukkit.getWorld("world"), 0, 65, 0, 0, 0);
			Location purchaselobbyloc = new Location(Bukkit.getWorld("world"), 100, 65, 100, 0, 0);

			conf.set("wartelobby", wartelobbyloc);
			conf.set("purchaselobby", purchaselobbyloc);
			
			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
				
				
			}
			
		}
		
		if(! file2.exists()) {
			
			Location specspawn = new Location(Bukkit.getWorld("world"), -100, 65, -100, 0, 0);
			Location playerspawn1 = new Location(Bukkit.getWorld("world"), 0, 65, 0, 0, 0);
			Location playerspawn2 = new Location(Bukkit.getWorld("world"), 50, 65, 0, 0, 0);
			
			conf2.set("spectatorspawn", specspawn);
			conf2.set("playerspawns.1", playerspawn1);
			conf2.set("playerspawns.2", playerspawn2);
			
			try {
				conf2.save(file2);
			} catch (IOException e) {
				e.printStackTrace();
				
				
			}
		}
	}


}
