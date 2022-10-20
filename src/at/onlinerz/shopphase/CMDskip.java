package at.onlinerz.shopphase;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class CMDskip implements CommandExecutor {
	
	public String prefix = TryJump.prefix;
	
	public static ArrayList<Player> votedSkip = new ArrayList<Player>();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("skip")) {
			if(TryJump.phase.equalsIgnoreCase("shopping")) {
				Player p = (Player)sender;
				
				if(! votedSkip.contains(p) && LobbyListener.participating.contains(p.getUniqueId())) {
					Bukkit.broadcastMessage(prefix+"§e"+p.getDisplayName()+ " §7hat dafür gestimmt, die Wartezeit zu verkürzen. §b("+(votedSkip.size()+1)+"/"+LobbyListener.participating.size()+")");
					votedSkip.add(p.getPlayer());
					
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.NOTE_PLING, 7, 7);
					}
					
					if(votedSkip.size() == LobbyListener.participating.size()) {
						ShopBoard.countdownins = 5;
						Bukkit.broadcastMessage(prefix+ "§eDie Wartezeit wird verkürzt. Das Deathmatch startet in wenigen Sekunden!");
						
						
					}
				}
				
			}
			
		}
		return false;
		
	}

}
