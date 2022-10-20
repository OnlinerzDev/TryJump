package at.onlinerz.general;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class PreventListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
			e.setCancelled(true);
			
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
			e.setCancelled(true);
			
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if(! TryJump.phase.equalsIgnoreCase("deathmatch")) {
		e.setCancelled(true);
		
		}
	}
	
	@EventHandler
    public void onDamage(EntityDamageEvent e) {
		if(TryJump.phase.equalsIgnoreCase("deathmatch") && LobbyListener.participating.contains(e.getEntity().getUniqueId())) {
			
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(TryJump.phase.equalsIgnoreCase("deathmatch") && LobbyListener.participating.contains(e.getPlayer().getUniqueId())) {
			
		} else {
			e.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(TryJump.phase.equalsIgnoreCase("deathmatch") && LobbyListener.participating.contains(e.getPlayer().getUniqueId())) {
			
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMoveItem(InventoryClickEvent e) {
		if(TryJump.phase.equalsIgnoreCase("deathmatch")) {
			
		} else {
			if(TryJump.phase.equalsIgnoreCase("shopping")) {
				if(e.getCurrentItem().getType() != Material.CHEST) {
					
				} else {
					e.setCancelled(true);
				}
				
				
			} else {
			e.setCancelled(true);
		}
			
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			if(SpectatorListener.spectator.contains(e.getEntity().getUniqueId()) || SpectatorListener.spectator.contains(e.getDamager().getUniqueId())) {
				e.setCancelled(true);
			
		}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(! LobbyListener.participating.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
}
