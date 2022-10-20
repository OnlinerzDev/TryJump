package at.onlinerz.jumpphase;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import at.onlinerz.general.LoadSchematics;
import at.onlinerz.general.ModuleOrder;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class JumpListener implements Listener {
	
	int itemcooldownins = 2;
	public static Boolean playerFinished = false;
	
	public static HashMap<UUID, Location> checkpoint = new HashMap<UUID, Location>();
	public static HashMap<UUID, Long> itemcooldown = new HashMap<UUID, Long>();
	public static HashMap<UUID, Long> platecooldown = new HashMap<UUID, Long>();
	public static HashMap<UUID, Integer> fails = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> currentModule = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> tokenbalance = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> totalfails = new HashMap<UUID, Integer>();

	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(TryJump.freeze == true && LobbyListener.participating.contains(p.getUniqueId()) && TryJump.phase.equalsIgnoreCase("waitToJump")) {
			Location from = checkpoint.get(p.getUniqueId());
			Location to = e.getPlayer().getLocation();
			if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY()){
				p.teleport(from);
	}
		
	} else {
		if(TryJump.phase.equalsIgnoreCase("jumpphase")) {
			double height = p.getLocation().getY();
			
			if(height < 0) {
				UUID uuid = p.getUniqueId();
				
				if(LobbyListener.participating.contains(uuid)) {
				int amount = fails.get(uuid);
				int newamount = amount+1;
				fails.replace(uuid, newamount);
				p.teleport(checkpoint.get(uuid));
				p.playSound(p.getLocation(), Sound.FALL_SMALL, 10, 10);
				itemcooldown.replace(uuid, System.currentTimeMillis());
				failcounter(p, newamount);
				
			
			} else {
				p.teleport(Bukkit.getPlayer(LobbyListener.participating.get(0)));
			}
				
			}
		}
	}
		
	}
	
	
	@EventHandler
	public void onRightclick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getItemInHand().getType() == Material.MAGMA_CREAM) {
				if(TryJump.phase.equalsIgnoreCase("jumpphase")) {
						if((itemcooldown.get(uuid)+itemcooldownins*1000) <= System.currentTimeMillis()) {
							int amount = fails.get(uuid);
							int newamount = amount+1;
							fails.replace(uuid, newamount);
							p.teleport(checkpoint.get(uuid));
							p.playSound(p.getLocation(), Sound.FALL_SMALL, 10, 10);
							itemcooldown.replace(uuid, System.currentTimeMillis());
							failcounter(p, newamount);
					
						} else {
							String prefix = TryJump.prefix;
							p.sendMessage(prefix+ "§cWarte, um diesen Item wieder benutzen zu können!");
					
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void failcounter(Player p, int amount) {
		
		if(amount <= 3) {
			String title = "§c✕ §7✕ ✕";
		
			if(amount == 2) {
			title = "§c✕ ✕ §7✕";
			
			}
		
			if(amount == 3) {
				title = "§c✕ ✕ ✕";
				
				UUID uuid = p.getUniqueId();
				Location loc = checkpoint.get(uuid);
				Location schemLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
				
				int current = currentModule.get(uuid);
				int modulenumber = ModuleOrder.moduleorder.get(current-1);
				
				Boolean simplified = hasSimplified(current);
				if(simplified) {
					GenerateSchematics.generateSimplified(schemLoc, modulenumber);
				}
			}
		
			p.sendTitle(title, "");

		
		}
		
			
		}
	
	public Boolean hasSimplified(int id) {
		if(LoadSchematics.modules.get(id).getSimplifiedschematicFile() == null) {
			return false;
			
		} else {
			return true;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPressureplate(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if(e.getAction() == Action.PHYSICAL && LobbyListener.participating.contains(uuid)) {
		if(TryJump.phase.equalsIgnoreCase("jumpphase")) {
			if((platecooldown.get(uuid)+2000) < System.currentTimeMillis()) {
				if(e.getClickedBlock().getType() == Material.IRON_PLATE) {
					e.setCancelled(true);
					
					// (cooldown verhindert doppeltes Triggern der Druckplatte)
					platecooldown.replace(uuid, System.currentTimeMillis());
					Location loc = e.getClickedBlock().getLocation();
					
						
					// get information
					int balance = tokenbalance.get(uuid);
					int failssum = fails.get(uuid);
					int current = currentModule.get(uuid);
					int moduleid = ModuleOrder.moduleorder.get(current-1);
					String difficulty = LoadSchematics.modules.get(moduleid).getDifficulty();
						
					// calculate reward 
					int reward = getRewardAmount(difficulty);
					if(failssum < 3) {
					reward = reward*2;
					}
						
						// reward player
						String prefix = TryJump.prefix;
						tokenbalance.replace(uuid, balance+reward);
						p.sendMessage(prefix+ "§e+" +reward+ " Tokens");
						// add ranked points?
						
						// add fails to total
						if(current == 1) {
							totalfails.put(uuid, failssum);
							
						} else {
							int total = totalfails.get(uuid);
							totalfails.replace(uuid, total+failssum);
							
							
							}
						
						// update scoreboard
						JumpBoard.changeBalanceForPlayer(p, balance, balance+reward);
						JumpBoard.changeRanking(p, current);
						
						// build modul
						if(ModuleOrder.moduleorder.size() > current) {
							int id = ModuleOrder.moduleorder.get(current);
							
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 10);
							GenerateSchematics.generateNext(loc, id);
						
							// adapt information to new module
							fails.replace(uuid, 0);
							loc.setY(loc.getY()+2);
							loc.setX(loc.getX()+0.3);
							loc.setZ(loc.getZ()+0.3);
							loc.setPitch(0);
							loc.setYaw(0);
							checkpoint.replace(uuid, loc);
							currentModule.replace(uuid, current+1);
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.pl, new Runnable() {
								
								@Override
								public void run() {
									loc.setY(loc.getY()-1);
									loc.getBlock().setType(Material.AIR);
									
									// remove drops
									for(Entity ent : p.getNearbyEntities(5, 5, 5)) {
										if(ent instanceof Item) {
											ent.remove();
											
										}
										
									}
									
								}
							}, 10);
						
						
						// Spieler erreicht Ziel
						} else {
							currentModule.remove(p.getUniqueId());
							if(! playerFinished) {
							playerFinished = true;
							JumpBoard.countdownins = 10;
							p.getInventory().clear();
							fails.replace(p.getUniqueId(), 4);
							
							sendTitle(p.getDisplayName());
							
							for(Player all : Bukkit.getServer().getOnlinePlayers()) {
								all.playSound(all.getLocation(), "mob.guardian.curse", 8, 8);
							}
							
							Bukkit.broadcastMessage(prefix+"§e" +p.getDisplayName()+ " §rhat als erstes das Ziel erreicht!");
							Bukkit.broadcastMessage(prefix+ "§eDie Shopping-Phase startet in 10 Sekunden!");
							
							}  else {
								Bukkit.broadcastMessage(prefix+"§e" +p.getDisplayName()+ " §rhat das Ziel erreicht!");
								// add ranked points?
							}
							
							if(hasZeroFails(p.getUniqueId())) {
								int currentbalance = tokenbalance.get(p.getUniqueId());
								tokenbalance.replace(p.getUniqueId(), currentbalance+2500);
								p.sendMessage(prefix+"§60 Fails! §e+2500 Tokens");
								JumpBoard.changeBalanceForPlayer(p, currentbalance, currentbalance+2500);
								
							} 
							
							checkpoint.replace(p.getUniqueId(), p.getLocation());
							
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.pl, new Runnable() {
								
								@Override
								public void run() {
									loc.getBlock().setType(Material.CAKE_BLOCK);
									
									
								}
							}, 10);
						}
						
					}
					
				
				}
			
		}
		
		}
		
	}
	
	public Integer getRewardAmount(String difficulty) {
		switch(difficulty) {
			case "Easy":
				return 100;
			case "Normal":
				return 150;
			case "Hard":
				return 200;
			case "Extreme":
				return 250;
			default:
				return 0;
			}
		
	}
	
	public Boolean hasZeroFails(UUID uuid) {
		int failamount = totalfails.get(uuid);
		
		if(failamount == 0) {
			return true;
			
		}
		
		return false;
		
	}
	
	public void sendTitle(String winnername) {
		IChatBaseComponent modeTitle = ChatSerializer.a("{\"text\": \"" +"§6"+winnername+ "\",color:" + ChatColor.YELLOW.name().toLowerCase() + "}");
		IChatBaseComponent serverTitle = ChatSerializer.a("{\"text\": \"" +"hat das Ziel erreicht!"+"\",color:" + ChatColor.AQUA.name().toLowerCase() + "}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, modeTitle);
		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, serverTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(7, 50, 7);
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {

			((CraftPlayer) all).getHandle().playerConnection.sendPacket(title);
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(subtitle);
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(length);
		}
	}
	
}
