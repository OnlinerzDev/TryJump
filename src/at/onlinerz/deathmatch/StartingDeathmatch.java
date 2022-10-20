package at.onlinerz.deathmatch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;

import at.onlinerz.general.PrepareWorlds;
import at.onlinerz.general.SpectatorListener;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class StartingDeathmatch {
	
	public static HashMap<Player, Location> firstarenaspawnpoint = new HashMap<Player, Location>();
	
	public static File file2 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	public static int countdown;
	public static int waittofightins = 6;
	
	@SuppressWarnings("deprecation")
	public static void startDeathmatchPhase() {
		TryJump.freeze = true;
		TryJump.phase = "waitToFight";
		
		ArrayList<Location> spawnpoints = PrepareWorlds.arenaspawnpoints;
		int spawns = spawnpoints.size()-1;
		
		ItemStack shopitem = new ItemStack(Material.CHEST);
		ItemMeta shopim = shopitem.getItemMeta();
		shopim.setDisplayName("§bShop");
		shopitem.setItemMeta(shopim);
		
		for(UUID uuid : LobbyListener.participating) {
			Player p = Bukkit.getPlayer(uuid);
				p.teleport(spawnpoints.get(spawns));
				firstarenaspawnpoint.put(p, spawnpoints.get(spawns));
				p.getInventory().remove(shopitem);
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 7, 7);
				p.setWalkSpeed(0);
				p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				
				spawns = spawns-1;
				// if still enough spawnpoints, forgive next, else forgive from new (it should be worst-case, that there are less spawnpoints than participants!)
				if(spawns < 0) {
					spawns = spawnpoints.size()-1;
					
				}

		}
		
		Location specspawn = (Location) conf2.get("spectatorspawn");
		for(UUID uuid : SpectatorListener.spectator) {
			Player spec = Bukkit.getPlayer(uuid);
			spec.teleport(specspawn);
		}
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			DeathmatchBoard.BaseBoard(all);
		}
		
		countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				waittofightins = waittofightins-1;
				
				if(waittofightins == 3 || waittofightins == 2 || waittofightins == 1) {
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.CLICK, 10, 10);
						all.sendTitle("§e"+waittofightins, "");
					}
				} else {
					if(waittofightins == 0) {
						Bukkit.getScheduler().cancelAllTasks();
						Bukkit.broadcastMessage(TryJump.prefix+ "§eLasset die Kämpfe beginnen!");
						TryJump.freeze = false;
						TryJump.phase = "deathmatch";
						
						DeathmatchBoard.Timer();
						
						for(Player all : Bukkit.getServer().getOnlinePlayers()) {
							all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 9, 9);
							all.sendTitle("§eLOS!", "");
							all.setWalkSpeed((float) 0.2);
							DeathmatchListener.lives.put(all, 3);
							
						}
					}
				}
				
			}
		}, 0, 20);
	}

}
