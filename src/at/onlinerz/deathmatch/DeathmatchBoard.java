package at.onlinerz.deathmatch;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class DeathmatchBoard {
	
	public static File file2 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	public static int countdownins = 10*60;
	static int counter;
	
	static double bordercountdown = 120.0;
	static int bordercounter;
	
	public static void BaseBoard(Player p) {
		String prefix = TryJump.prefix;
	    ScoreboardManager manager = Bukkit.getScoreboardManager();
	    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
	    Objective objective = board.registerNewObjective("LivesScoreboard", "dummy");
	    objective.setDisplayName(prefix+ "§b10:00");
	    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    
	    
	    for(UUID s : LobbyListener.participating) {
	    	Player players = Bukkit.getPlayer(s);
	    		String playername = players.getDisplayName();
	    		objective.getScore(playername+ " §4❤❤❤").setScore(3);
	    		
	    	}
	    
	    p.setScoreboard(board);
	    
	    }
	
	public static void Timer() {
		String prefix = TryJump.prefix;
		Location loc = (Location) conf2.get("spectatorspawn");
		loc.getWorld().getWorldBorder().setCenter(loc);
		loc.getWorld().getWorldBorder().setSize(300);
		HashMap<Player, Double> beacondist = new HashMap<Player, Double>();
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				countdownins = countdownins-1;
				

				if(countdownins == 190) {
					Bukkit.broadcastMessage(prefix+ "§eDie Border schrumpft in wenigen Sekunden!");
					for(UUID uuids : LobbyListener.participating) {
						Player all = Bukkit.getPlayer(uuids);
						all.playSound(all.getLocation(), Sound.ORB_PICKUP, 5, 5);
					}
				} else {
				if(countdownins == 180) {
					loc.getWorld().getWorldBorder().setCenter(loc);
					deathmatchborder(loc.getWorld());
					
				} else {
					if(countdownins == 20 || countdownins == 10 || countdownins == 5 || countdownins == 3 || countdownins == 2 || countdownins == 1) {
						Bukkit.broadcastMessage(prefix+"§7Der Spieler, der in §e"+countdownins+" Sekunden §7am nähesten am §bBeacon §7ist, gewinnt!");
						for(UUID uuids : LobbyListener.participating) {
							Player all = Bukkit.getPlayer(uuids);
							all.playSound(all.getLocation(), Sound.ORB_PICKUP, 10, 10);
						}
					} else {
						if(countdownins == 0) {
							
							for(UUID uuids : LobbyListener.participating) {
								Player participating = Bukkit.getPlayer(uuids);
								
								Location plloc = participating.getLocation();
								double dist = Math.abs(loc.getBlockX() - plloc.getX()) + Math.abs(loc.getBlockZ() - plloc.getZ());
								beacondist.put(participating, dist);	
							}
							
							// evaluate smallest distance
							double min = Collections.min(beacondist.values());
							
							for(Entry<Player, Double> e : beacondist.entrySet()) {
								if(e.getValue() == min) {
									Player winner = e.getKey();
									
									Bukkit.getScheduler().cancelAllTasks();
									Bukkit.broadcastMessage(prefix+ "§6"+winner.getDisplayName()+ " §ehat das Spiel gewonnen!");
									Bukkit.getPlayer(LobbyListener.participating.get(0)).playSound(winner.getLocation(), Sound.ENDERDRAGON_DEATH, 5, 5);
									DeathmatchListener.fireRockets();
									TryJump.phase = "ending";
									
									// add stats points?
								}
								
							}
						}
					}
				}
				}

				int remainingMin = countdownins/60;
				int remainingSek = countdownins%60;
				
				String min = Integer.toString(remainingMin);
				String sek = Integer.toString(remainingSek);
				if(remainingMin < 10) {
					min = "0"+remainingMin;
				}
				
				if(remainingSek < 10) {
					sek = "0"+remainingSek;
				}
				
				for(Player all : Bukkit.getServer().getOnlinePlayers()) {
					
						org.bukkit.scoreboard.Scoreboard board = all.getScoreboard();
						Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
					
						objective.setDisplayName(prefix+ "§b"+min+":"+sek);
					
				}
				
			}
				
		}, 0, 20);
		
	}
		
	
	public static void changeLives(Player losinglive, int newamount) {
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {

			org.bukkit.scoreboard.Scoreboard board = all.getScoreboard();
			Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
			
			String livedisplay = getHeartsDisplay(newamount);
			board.resetScores(losinglive.getDisplayName()+ " §4❤❤❤");
			board.resetScores(losinglive.getDisplayName()+ " §4❤❤§8❤");
			board.resetScores(losinglive.getDisplayName()+ " §4❤§8❤❤");
			objective.getScore(losinglive.getDisplayName()+" "+livedisplay).setScore(newamount);
			

		}
		
	}

	public static void deathmatchborder(World w) {
		
		bordercounter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				bordercountdown = bordercountdown-0.5;
				
				if(bordercountdown == 0) {
					Bukkit.getScheduler().cancelTask(bordercounter);
				}
				
				double size = 2.16*bordercountdown+30;
				w.getWorldBorder().setSize(size);
				
				for(UUID uuids : LobbyListener.participating) {
					Player all = Bukkit.getPlayer(uuids);
					PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText("§7» §eBorder schrumpft §7«"), (byte)2);
					((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				}
				
			}
		}, 0, 10);
	}
	
	public static String getHeartsDisplay(int lives) {
		if(lives == 2) {
			return "§4❤❤§8❤";
			
		} else {
			if(lives == 1) {
				return "§4❤§8❤❤";
				
			} else {
				if(lives == 3) {
					return "§4❤❤❤";
				} else {
				return null;
				
				}
			}
		}
	}

}
