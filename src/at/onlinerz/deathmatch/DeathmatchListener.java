package at.onlinerz.deathmatch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.onlinerz.general.PrepareWorlds;
import at.onlinerz.general.SpectatorListener;
import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class DeathmatchListener implements Listener {
	
	public static String prefix = TryJump.prefix;
	public static int task;
	public static int repeats = 15;
	
	public static File file2 = new File("plugins/TryJump/Locations/deathmatcharena.yml");
	public static YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
	
	public static HashMap<Player, Integer> lives = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(TryJump.freeze == true && TryJump.phase.equalsIgnoreCase("waitToFight")) {
			Location from = StartingDeathmatch.firstarenaspawnpoint.get(p);
			Location to = e.getPlayer().getLocation();
			if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()){
				p.teleport(from);
				
			}
			
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(TryJump.phase.equalsIgnoreCase("deathmatch")) {
			e.setDeathMessage("");
			
			int livesremaining = lives.get(p);
			lives.replace(p, (livesremaining-1));
			
			if(p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
				p.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 2));
				
			}

			if(livesremaining > 1) {
				e.setKeepInventory(true);
				respawning(p);
			    DeathmatchBoard.changeLives(p, livesremaining-1);
			    
			} else {
				LobbyListener.participating.remove(p.getUniqueId());
				p.sendMessage("§7Du bist ausgeschieden!");
				
				for(Player all : Bukkit.getServer().getOnlinePlayers()) {
					all.getScoreboard().resetScores(p.getDisplayName()+ " §4❤§8❤❤");
				
				
				}
				
				disqualify(p);
				
			}
			
		} else {
			JumpListener.checkpoint.get(p.getUniqueId());
		}
	}
	
	public static void fireRockets() {
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				repeats = repeats-1;
				
				Player p = Bukkit.getPlayer(LobbyListener.participating.get(0));
				
				if(p.isOnline()) {
		        Location loc = p.getLocation();
				
		        if(repeats == 2) {
		        	Bukkit.broadcastMessage(prefix+ "§eServer wird heruntergefahren..");
		        }
				if(repeats > 0) {
		        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		        FireworkMeta fwm = fw.getFireworkMeta();
		       
		        fwm.setPower(2);
		        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
		       
		        fw.setFireworkMeta(fwm);
		        fw.detonate();
		       
		        for(int i = 0;i<10; i++){
		            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		            fw2.setFireworkMeta(fwm);
		        }
				} else {
					Bukkit.getScheduler().cancelAllTasks();
					loc.getWorld().getWorldBorder().setSize(1000);
					Bukkit.shutdown();
					
				}
				}
			}
		}, 10, 40);
		
	}
	
	public void respawning(Player p) {
		
		int time = DeathmatchBoard.countdownins;
		Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				
				if(p.isOnline()) {
				
				((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				
				if(PrepareWorlds.safearenaspawnpoints.isEmpty() || time > 180) {
					ArrayList<Location> list = PrepareWorlds.arenaspawnpoints;
					Random rand = new Random();
					int randomnum = rand.nextInt(list.size());
			    
					p.teleport(list.get(randomnum));
			    
				} else {
					ArrayList<Location> list = PrepareWorlds.safearenaspawnpoints;
					Random rand = new Random();
					int randomnum = rand.nextInt(list.size());
			    
					p.teleport(list.get(randomnum));
				}
			    
				}
				
			}
		}, 2);
		
	}
	
	public void disqualify(Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				
				for(UUID uuid : LobbyListener.participating) {
					Player all = Bukkit.getPlayer(uuid);
					all.hidePlayer(p);
				}
				
				p.setAllowFlight(true);
				p.setFlying(true);
				p.setFlySpeed((float) 0.2);
				Location specspawn = (Location) conf2.get("spectatorspawn");
				p.teleport(specspawn);
				SpectatorListener.spectator.add(p.getUniqueId());
				
				ItemStack i = new ItemStack(Material.COMPASS);
				ItemMeta im = i.getItemMeta();
				im.setDisplayName("§bKompass");
				i.setItemMeta(im);
				
				p.getInventory().setItem(4, i);
				
				if(LobbyListener.participating.size() > 1) {
					Bukkit.broadcastMessage(prefix+ "§7Noch §e"+LobbyListener.participating.size()+ " Spieler §7verbleibend!");
					
				} else {
					Bukkit.getScheduler().cancelAllTasks();
					if(! LobbyListener.participating.isEmpty()) {
					Player winner = Bukkit.getPlayer(LobbyListener.participating.get(0));
					Bukkit.broadcastMessage(prefix+ "§6"+winner.getDisplayName()+ " §ehat das Spiel gewonnen!");
					Bukkit.getPlayer(LobbyListener.participating.get(0)).playSound(winner.getLocation(), Sound.ENDERDRAGON_DEATH, 5, 5);
					fireRockets();
					TryJump.phase = "ending";
					
					// add stats points?
					
					}
				}
			}
		}, 2);
	}
	
}
