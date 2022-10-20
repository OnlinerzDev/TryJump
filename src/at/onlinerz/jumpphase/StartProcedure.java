package at.onlinerz.jumpphase;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import at.onlinerz.general.PrepareWorlds;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class StartProcedure {
	
	static int counter;
	static int countdown = 6;
	
	public static void onStart() {
		int looped = 0;
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			LobbyListener.participating.add(all.getUniqueId());
			all.playSound(all.getLocation(), Sound.LEVEL_UP, 9, 9);
			Location loc = PrepareWorlds.spawnpoints.get(looped);
			loc.setPitch(0);
			loc.setYaw(0);
			
			all.getInventory().clear();
			loc.setX(loc.getX()+0.3);
			loc.setY(loc.getY()+1.0);
			loc.setZ(loc.getZ()+0.3);
			all.teleport(loc);
			all.setWalkSpeed(0);
			
			UUID uuid = all.getUniqueId();
			JumpListener.checkpoint.put(uuid, loc);
			JumpListener.currentModule.put(uuid, 1);
			JumpListener.tokenbalance.put(uuid, 0);
			JumpListener.fails.put(uuid, 0);
			JumpListener.itemcooldown.put(uuid, System.currentTimeMillis());
			JumpListener.platecooldown.put(uuid, System.currentTimeMillis());
			
		    all.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

			looped = looped+1;
			
		}
		
		for(Player all: Bukkit.getServer().getOnlinePlayers()) {
		    JumpBoard.BaseBoard(all);
		}
		
		TryJump.freeze = true;
		countdown();
		JumpBar.TimerTask();
		
	}
	
	public static void countdown() {
		
		String prefix = TryJump.prefix;
		
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				countdown = countdown-1;
				
				if(countdown > 0 && countdown <= 3) {
				Bukkit.broadcastMessage(prefix+ "§7Die Runde startet in §e" +countdown+ " Sekunden!");
				
				for(UUID uuid : LobbyListener.participating) {
					Player all = Bukkit.getPlayer(uuid);
					
					all.playSound(all.getLocation(), Sound.ORB_PICKUP, 10, 10);
					
				}
				
				} else {
					if(countdown == 0) {
					TryJump.freeze = false;
					TryJump.phase = "jumpphase";
					Bukkit.broadcastMessage(prefix+ "§eDas Spiel beginnt!");
					Bukkit.getScheduler().cancelTask(counter);
					countdown = 6;
					
					
					ItemStack i = new ItemStack(Material.MAGMA_CREAM);
					ItemMeta im = i.getItemMeta();
					im.setDisplayName("§c§lInstant-Tod §7<Rechtsklick>");
					i.setItemMeta(im);
					
					for(UUID uuids : LobbyListener.participating) {
						Player participating = Bukkit.getPlayer(uuids);
						participating.playSound(participating.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 10);
						participating.setWalkSpeed((float) 0.2);
						participating.getInventory().setItem(4, i);
						
					
					}
					
				    JumpBoard.Timer();
				}
					
				}
				
			}
		}, 0, 20);
	}

}
