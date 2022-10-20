package at.onlinerz.lobbyphase;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.onlinerz.general.MainConfig;
import at.onlinerz.jumpphase.StartProcedure;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class LobbyListener implements Listener {
	
	public static File file = new File("plugins/TryJump/Locations/lobbies.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file);
	
	public static ArrayList<UUID> participating = new ArrayList<UUID>();
	
	int counter;
	int countdown = 30;
	YamlConfiguration conf = MainConfig.conf;
	public static boolean countdownrunning = false;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setWalkSpeed((float) 0.2);
		p.setLevel(0);
		if(TryJump.phase.equalsIgnoreCase("waiting")) {
		String prefix = TryJump.prefix;
		if(!TryJump.running) {
			e.setJoinMessage(TryJump.joinmsg.replace("%player%", e.getPlayer().getDisplayName()));
			p.setMaxHealth(20);
			p.setHealth(20);
			p.setFoodLevel(20);
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			
			Location lobbyspawn = (Location) conf2.get("wartelobby");
			p.teleport(lobbyspawn);
			int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
			int limit = conf.getInt("PlayersToStart");
			if(onlinePlayers >= limit) {
			if(! countdownrunning) {
				Bukkit.broadcastMessage(prefix+ "§7Das Spiel startet in §e30 Sekunden.");
				LobbyCountdown();
				
			}
		} else {
			int fehlend = limit-onlinePlayers;
			Bukkit.broadcastMessage(prefix+ "§7Noch §e" +fehlend+ " Spieler §7um den Countdown zu starten.");
		}
		} 
		
		} else {
			e.setJoinMessage("");
		}
	}
	
	@EventHandler 
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		if(TryJump.phase.equalsIgnoreCase("waiting")) {
			String prefix = TryJump.prefix;
			e.setQuitMessage(TryJump.leavemsg.replace("%player%", e.getPlayer().getDisplayName()));
			int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size()-1;
			int limit = conf.getInt("PlayersToStart");

			if(countdownrunning == true) {
				if(onlinePlayers >= limit) {
				
				} else {
					Bukkit.getScheduler().cancelTask(counter);
					countdownrunning = false;
					Bukkit.broadcastMessage(prefix+ "§eZu wenige Spieler. Der Countdown wurde abgebrochen.");
				
						
			}
			
		}
		
		} else {
			e.setQuitMessage("");
		}
		
	}
	
	public void LobbyCountdown() {
		
		sendTitle();
		String prefix = TryJump.prefix;
		countdownrunning = true;
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() { // COUNTDOWN
			
			@Override
			public void run() {

				countdown = countdown-1;
				for(Player all : Bukkit.getServer().getOnlinePlayers()) {
					all.setLevel(countdown);
				}
				
				if(countdown == 20 || countdown == 10) {
					Bukkit.broadcastMessage(prefix+ "§7Das Spiel startet in §e" +countdown+ " Sekunden.");
					
					for(Player all : Bukkit.getServer().getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.CLICK, 7, 7);
					}
						
					} else {
					if(countdown < 6 && countdown > 0) {
						Bukkit.broadcastMessage(prefix+ "§7Das Spiel startet in §e" +countdown+ " Sekunden.");
						for(Player all : Bukkit.getServer().getOnlinePlayers()) {
							all.playSound(all.getLocation(), Sound.NOTE_PLING, 8, 8);
						}
					} else {
						if(countdown == 0) {
							TryJump.running = true;
							
							TryJump.phase = "waitToJump";
							Bukkit.broadcastMessage(prefix+ "§eDas Spiel wird gestartet!");
							Bukkit.getScheduler().cancelTask(counter);
							countdown = 30;
							StartProcedure.onStart();
							
						}
					}
				}
				
			}
		}, 0, 20);
	}
	
	public void sendTitle() {
		String servername = conf.getString("ServerName");
		IChatBaseComponent modeTitle = ChatSerializer.a("{\"text\": \"" +"Tryjump"+ "\",color:" + ChatColor.YELLOW.name().toLowerCase() + "}");
		IChatBaseComponent serverTitle = ChatSerializer.a("{\"text\": \"" +"auf "+servername+ "\",color:" + ChatColor.AQUA.name().toLowerCase() + "}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, modeTitle);
		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, serverTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			all.playSound(all.getLocation(), Sound.ANVIL_LAND, 8, 8);

			((CraftPlayer) all).getHandle().playerConnection.sendPacket(title);
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(subtitle);
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(length);
		}
	}

}
