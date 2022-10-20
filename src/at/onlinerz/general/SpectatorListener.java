package at.onlinerz.general;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import at.onlinerz.deathmatch.DeathmatchBoard;
import at.onlinerz.deathmatch.DeathmatchListener;
import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class SpectatorListener implements Listener {
	
	public String prefix = TryJump.prefix;
	
	public static ArrayList<UUID> spectator = new ArrayList<UUID>();
	public static HashMap<UUID, String> loggedout = new HashMap<UUID, String>();
	
	public static File file = new File("plugins/TryJump/Locations/lobbies.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static File file2 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		if(! TryJump.phase.equalsIgnoreCase("waiting")) {
		e.setQuitMessage("");
		Player p = e.getPlayer();
		
		if(LobbyListener.participating.contains(p.getUniqueId())) {
			LobbyListener.participating.remove(p.getUniqueId());
			
			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.getScoreboard().resetScores(p.getDisplayName());
			}
			
			if(LobbyListener.participating.size() < 2) {
				Bukkit.getScheduler().cancelAllTasks();
				if(! LobbyListener.participating.isEmpty()) {
					Player winner = Bukkit.getPlayer(LobbyListener.participating.get(0));
					Bukkit.broadcastMessage(prefix+ "§6"+winner.getDisplayName()+ " §ehat das Spiel gewonnen!");
					Bukkit.getPlayer(LobbyListener.participating.get(0)).playSound(winner.getLocation(), Sound.ENDERDRAGON_DEATH, 5, 5);
					DeathmatchListener.fireRockets();
					TryJump.phase = "ending";
				
					// add stats points?
				
				}
			}
			
			if(! TryJump.phase.equalsIgnoreCase("deathmatch")) {
				loggedout.put(p.getUniqueId(), TryJump.phase);
			}
		
		}
		}
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(! TryJump.phase.equalsIgnoreCase("waiting")) {
			if(loggedout.containsKey(p.getUniqueId())) {
				if(loggedout.get(p.getUniqueId()).equalsIgnoreCase(TryJump.phase)) {
					LobbyListener.participating.add(p.getUniqueId());
				
					setCurrentPlayerScoreboard(p);
					addToScoreboard(p);
			
			} else {
				setCurrentSpectatorScoreboard(p);
				spectator.add(p.getUniqueId());
				p.setAllowFlight(true);
				p.setFlying(true);
				
				p.sendMessage(prefix+"§7Die Runde wurde bereits gestartet. Du kannst nun als §eSPECTATOR §7zusehen.");
				
				ItemStack i = new ItemStack(Material.COMPASS);
				ItemMeta im = i.getItemMeta();
				im.setDisplayName("§bKompass");
				i.setItemMeta(im);
				
				p.getInventory().setItem(4, i);
				
				for(UUID uuid : LobbyListener.participating) {
					Player all = Bukkit.getPlayer(uuid);
					
					all.hidePlayer(p);
				
				}
			}
		} else {
			setCurrentSpectatorScoreboard(p);
			spectator.add(p.getUniqueId());
			p.setAllowFlight(true);
			p.setFlying(true);
			
			p.sendMessage(prefix+"§7Die Runde wurde bereits gestartet. Du kannst nun als §eSPECTATOR §7zusehen.");
			
			ItemStack i = new ItemStack(Material.COMPASS);
			ItemMeta im = i.getItemMeta();
			im.setDisplayName("§bKompass");
			i.setItemMeta(im);
			
			p.getInventory().setItem(4, i);
			
			for(UUID uuid : LobbyListener.participating) {
				Player all = Bukkit.getPlayer(uuid);
				
				all.hidePlayer(p);
			
			}
			
		}
		}
	
	}
	
	public void addToScoreboard(Player p) {
		if(TryJump.phase.equalsIgnoreCase("jumping")) {
			
			for(Player all : Bukkit.getServer().getOnlinePlayers()) {
				if(JumpListener.currentModule.containsKey(p.getUniqueId())) {
				all.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p.getDisplayName()).setScore(JumpListener.currentModule.get(p.getUniqueId()));
				
				} else {
					all.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p.getDisplayName()).setScore(0);
				}
			}
			
		} else if(TryJump.phase.equalsIgnoreCase("shopping")) {
			
			for(Player all : Bukkit.getServer().getOnlinePlayers()) {
				all.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p.getDisplayName()).setScore(JumpListener.tokenbalance.get(p.getUniqueId()));
			}
		}
	}
	
	public void setCurrentPlayerScoreboard(Player p) {
		
		if(TryJump.phase.equalsIgnoreCase("jumpphase")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("JumpScoreboard", "dummy");
		    objective.setDisplayName(prefix);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    objective.getScore(" ").setScore(14);
		    objective.getScore("§e§lTokens:").setScore(13);
		    
		    objective.getScore("§7" +JumpListener.tokenbalance.get(p.getUniqueId())).setScore(12);
		    objective.getScore("  ").setScore(11);
		    
		    if(JumpListener.currentModule.containsKey(p.getUniqueId())) {
		    	objective.getScore("§n"+p.getDisplayName()).setScore(JumpListener.currentModule.get(p.getUniqueId()));
		    	
		    } else {
		    	objective.getScore("§n"+p.getDisplayName()).setScore(0);
		    }
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player rest = Bukkit.getPlayer(s);
		    	if(! p.equals(rest)) {
		    		String playername = rest.getDisplayName();
		    		if(JumpListener.currentModule.containsKey(p.getUniqueId())) {
		    			objective.getScore(playername).setScore(JumpListener.currentModule.get(s));
		    			
		    		}
		    		
		    	}
		    	
		    }
		    
		    p.setScoreboard(board);
			
		} else if(TryJump.phase.equalsIgnoreCase("shopping")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("ShopScoreboard", "dummy");
		    objective.setDisplayName(prefix);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player rest = Bukkit.getPlayer(s);
		    		String playername = rest.getDisplayName();
		    		int balance = JumpListener.tokenbalance.get(s);
		    		objective.getScore(playername).setScore(balance);
		    		
		    	}
		    
		    p.setScoreboard(board);
			
		} else if(TryJump.phase.equalsIgnoreCase("deathmatch")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("LivesScoreboard", "dummy");
		    objective.setDisplayName(prefix+DeathmatchBoard.countdownins);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player players = Bukkit.getPlayer(s);
		    		String playername = players.getDisplayName();
		    		int lives = DeathmatchListener.lives.get(players);
		    		String hearts = DeathmatchBoard.getHeartsDisplay(lives);
		    		objective.getScore(playername+ " "+hearts).setScore(lives);
		    		
		    	}
		    
		    p.setScoreboard(board);
			
		}
	}
	
	public void setCurrentSpectatorScoreboard(Player p) {
		
		if(TryJump.phase.equalsIgnoreCase("jumpphase")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("JumpScoreboard", "dummy");
		    objective.setDisplayName(prefix);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    objective.getScore(" ").setScore(14);
		    objective.getScore("§e§lTokens:").setScore(13);
		    
		    objective.getScore("§70").setScore(12);
		    objective.getScore("  ").setScore(11);
	
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player rest = Bukkit.getPlayer(s);
		    	if(! p.equals(rest)) {
		    		String playername = rest.getDisplayName();
		    			objective.getScore(playername).setScore(JumpListener.currentModule.get(s));
		    		
		    	}
		    	
		    }
		    
		    p.setScoreboard(board);
		    Player first = Bukkit.getPlayer(LobbyListener.participating.get(0));
		    p.teleport(first.getLocation());
			
		} else if(TryJump.phase.equalsIgnoreCase("shopping")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("ShopScoreboard", "dummy");
		    objective.setDisplayName(prefix);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player rest = Bukkit.getPlayer(s);
		    		String playername = rest.getDisplayName();
		    		int balance = JumpListener.tokenbalance.get(s);
		    		objective.getScore(playername).setScore(balance);
		    		
		    	}
		    
		    p.setScoreboard(board);
		    Location purchaseloc = (Location) conf.get("purchaselobby");
		    p.teleport(purchaseloc);
			
		} else if(TryJump.phase.equalsIgnoreCase("deathmatch")) {
			
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
		    Objective objective = board.registerNewObjective("LivesScoreboard", "dummy");
		    objective.setDisplayName(prefix+DeathmatchBoard.countdownins);
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    
		    
		    for(UUID s : LobbyListener.participating) {
		    	Player players = Bukkit.getPlayer(s);
		    		String playername = players.getDisplayName();
		    		int lives = DeathmatchListener.lives.get(players);
		    		String hearts = DeathmatchBoard.getHeartsDisplay(lives);
		    		objective.getScore(playername+ " "+hearts).setScore(lives);
		    		
		    	}
		    
		    p.setScoreboard(board);
		    Location specspawnloc = (Location) conf2.get("spectatorspawn");
		    p.teleport(specspawnloc);
			
		} else {
			Player p1 = Bukkit.getPlayer(LobbyListener.participating.get(0));
			p.teleport(p1);
		}
	}
}
