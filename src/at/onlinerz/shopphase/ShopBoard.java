package at.onlinerz.shopphase;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import at.onlinerz.deathmatch.StartingDeathmatch;
import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class ShopBoard {
	
	static int counter;
	public static int countdownins = 90;
	
	
	public static void BaseBoard(Player p) {
		String prefix = TryJump.prefix;
	    ScoreboardManager manager = Bukkit.getScoreboardManager();
	    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
	    Objective objective = board.registerNewObjective("ShopScoreboard", "dummy");
	    objective.setDisplayName(prefix+ "§b01:30");
	    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    
	    
	    for(UUID s : LobbyListener.participating) {
	    	Player rest = Bukkit.getPlayer(s);
	    		String playername = rest.getDisplayName();
	    		int balance = JumpListener.tokenbalance.get(s);
	    		objective.getScore(playername).setScore(balance);
	    		
	    	}
	    
	    p.setScoreboard(board);
	    
	    }
	
	public static void Timer() {
		String prefix = TryJump.prefix;
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				countdownins = countdownins-1;
				
				if(countdownins == 0) {
					Bukkit.getScheduler().cancelAllTasks();
					Bukkit.broadcastMessage(prefix+ "§7Du wirst in die Deathmatch-Arena teleportiert.");

					StartingDeathmatch.startDeathmatchPhase();
					
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
				
				for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			
						org.bukkit.scoreboard.Scoreboard board = all.getScoreboard();
						Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
					
						objective.setDisplayName(prefix+ "§b"+min+":"+sek);
					}
					
				}
				
				if(countdownins == 3 || countdownins == 2 || countdownins == 1) {
					Bukkit.broadcastMessage(prefix+ "§7Das §eDeathmatch §7beginnt in §e"+countdownins+" Sekunden.");
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.NOTE_PLING, 8, 8);
					}
				} else if(countdownins == 40) {
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						if(! CMDskip.votedSkip.contains(all)) {
							all.sendMessage(prefix+ "§7Mit §b/skip §7kannst du dafür stimmen, die Wartezeit zu verkürzen!");
						}
					}
				}
				
			}
		}, 0, 20);
	}
	
	public static void changeBalance(Player rankingdown, int newbalance) {
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {

			org.bukkit.scoreboard.Scoreboard board = all.getScoreboard();
			Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
			
			board.resetScores(rankingdown.getDisplayName());
			objective.getScore(rankingdown.getDisplayName()).setScore(newbalance);
			

		}
		
	}

}
