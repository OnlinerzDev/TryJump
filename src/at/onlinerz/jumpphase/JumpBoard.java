package at.onlinerz.jumpphase;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.shopphase.InitiatingShopPhase;
import at.onlinerz.tryjump.TryJump;

public class JumpBoard {
	
	public static int countdowninmin = 10;
	public static int countdownins = countdowninmin*60;
	public static int counter;
	
	static String prefix = TryJump.prefix;
	
	public static void BaseBoard(Player p) {
	    ScoreboardManager manager = Bukkit.getScoreboardManager();
	    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
	    Objective objective = board.registerNewObjective("JumpScoreboard", "dummy");
	    objective.setDisplayName(prefix+ "§b10:00");
	    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    
	    objective.getScore(" ").setScore(14);
	    objective.getScore("§e§lTokens:").setScore(13);
	    objective.getScore("§70").setScore(12);
	    objective.getScore("  ").setScore(11);
	    
	    objective.getScore("§n"+p.getDisplayName()).setScore(0);
	    
	    for(UUID s : LobbyListener.participating) {
	    	Player rest = Bukkit.getPlayer(s);
	    	if(! p.equals(rest)) {
	    		String playername = rest.getDisplayName();
	    			objective.getScore(playername).setScore(0);
	    		
	    	}
	    	
	    }
	    
	    p.setScoreboard(board);
	    
	    }

	
	public static void changeBalanceForPlayer(Player p, int oldbalance, int newbalance) {
		org.bukkit.scoreboard.Scoreboard board = p.getScoreboard();
		Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
		
		board.resetScores("§7"+oldbalance);
		objective.getScore("§7"+newbalance).setScore(12);
		
	}
	
	public static void changeRanking(Player rankingup, int checkpoints) {
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {

			org.bukkit.scoreboard.Scoreboard board = all.getScoreboard();
			Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
			
			board.resetScores(rankingup.getDisplayName());
			
			if(! rankingup.equals(all)) {
			objective.getScore(rankingup.getDisplayName()).setScore(checkpoints);
			
			
		} else {
			objective.getScore("§n"+rankingup.getDisplayName()).setScore(checkpoints);
		}
		}
		
	}
	
	public static void Timer() {
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				countdownins = countdownins-1;
				
				if(countdownins == 0) {
					Bukkit.getScheduler().cancelAllTasks();
					InitiatingShopPhase.initiatingShopphase();
					
				} else {
				
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
				
				for(UUID uuid : LobbyListener.participating) {
					Player p = Bukkit.getPlayer(uuid);
					
					if(p != null) {
						if(p.isOnline()) {
						org.bukkit.scoreboard.Scoreboard board = p.getScoreboard();
						Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
					
						objective.setDisplayName(prefix+ "§b"+min+":"+sek);
						}
					}
					
				}
				
				if(countdownins == 3 || countdownins == 2 || countdownins == 1) {
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.SUCCESSFUL_HIT, 8, 8);
					}
				}
				
				// remove items laying in the world
				for(Entity ent : Bukkit.getWorld("jumpworld").getEntities()) {
					if(ent instanceof Item) {
						ent.remove();
					}
				}
				}
				
			}
		}, 0, 20);
	}
}
