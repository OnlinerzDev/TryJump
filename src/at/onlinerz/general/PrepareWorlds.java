package at.onlinerz.general;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;

@SuppressWarnings("deprecation")
public class PrepareWorlds {
	
	public static ArrayList<Location> spawnpoints = new ArrayList<Location>();
	public static ArrayList<Location> arenaspawnpoints = new ArrayList<Location>();
	public static ArrayList<Location> safearenaspawnpoints = new ArrayList<Location>();
	static YamlConfiguration conf = MainConfig.conf;
	
	public static File file1 = new File("plugins/TryJump/Locations/worlds.yml");
	public static YamlConfiguration conf1 = YamlConfiguration.loadConfiguration(file1);
	
	public static File file2 = new File("plugins/TryJump/Locations/lobbies.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	public static File file3 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf3 = YamlConfiguration.loadConfiguration(file3);
	
	public static void createWorld() {
		String wname = "jumpworld";
		if(Bukkit.getServer().getWorld(wname) == null) {
			WorldCreator wc = new WorldCreator("jumpworld");
			wc.type(WorldType.FLAT);
			wc.generateStructures(false);
			wc.generatorSettings("2;0;1");
			wc.createWorld();
			
			setWorldRules(Bukkit.getWorld(wname));
		}
		}

	public static void generateSpawnpoints() {
		
		// jump world spawnpoints
		int y = conf.getInt("SpawnPointYCoordinate");
		int xperiod = conf.getInt("SpawnPointDistance");
		
		int SpawnsToGo = conf.getInt("MaxPlayerAmount");
		
		while(SpawnsToGo > 0) {
			int x = (SpawnsToGo*xperiod)-xperiod;
			spawnpoints.add(new Location(Bukkit.getWorld("jumpworld"), x, y, 0));
			SpawnsToGo = SpawnsToGo-1;
		}
		
		// deathmatcharena spawnpoints
		
		Location specloc = (Location) conf3.get("spectatorspawn");
		for(String sections : conf3.getConfigurationSection("playerspawns").getKeys(false)) {

			Location loc = (Location) conf3.get("playerspawns."+sections);
			arenaspawnpoints.add(loc);
			
			if(Math.abs(specloc.getBlockX() - loc.getBlockX()) < 15) {
				if(Math.abs(specloc.getBlockZ() - loc.getBlockZ()) < 15) {
					safearenaspawnpoints.add(loc);
				}
			}
			
			
		}
		
	}
	
	public static void generateFirstModul() {
		
		for(Location loc : spawnpoints) {
			
			Location schemLoc = new Location(loc.getWorld(), loc.getX(), loc.getY()+1, loc.getZ());
		
			try {
				int index = ModuleOrder.moduleorder.get(0);
				File dir = LoadSchematics.modules.get(index).getSchematicFile();

				EditSession editSession = new EditSession(new BukkitWorld(schemLoc.getWorld()), 999999999);
				editSession.enableQueue();

				SchematicFormat schematic = SchematicFormat.getFormat(dir);
				CuboidClipboard clipboard = schematic.load(dir);

					clipboard.paste(editSession, BukkitUtil.toVector(schemLoc), true);
					editSession.flushQueue();
			} catch (DataException | IOException ex) {
				ex.printStackTrace();
			} catch (MaxChangedBlocksException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	public static void toSetRules() {
		// set rules for all worlds 

		Location purchaselobbyloc = (Location) conf2.get("purchaselobby");
		Location spectatorspawnloc = (Location) conf3.get("spectatorspawn");
		Location wartelobbyloc = (Location) conf2.get("wartelobby");
		
		setWorldRules(wartelobbyloc.getWorld());
		setWorldRules(purchaselobbyloc.getWorld());
		setWorldRules(spectatorspawnloc.getWorld());
	}
	
	public static void setWorldRules(World w) {
		
		w.setGameRuleValue("doDaylightCycle", "false");
		w.setGameRuleValue("doMobSpawning", "false");
		w.setStorm(false);
		w.setThundering(false);
		w.setWeatherDuration(1999999999);
		w.setTime(1000);
	}
}
