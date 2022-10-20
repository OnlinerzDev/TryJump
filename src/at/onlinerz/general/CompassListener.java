package at.onlinerz.general;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import at.onlinerz.lobbyphase.LobbyListener;

public class CompassListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(SpectatorListener.spectator.contains(p.getUniqueId())) {
				Inventory inv = Bukkit.createInventory(null, 3*9, "§bKompass");
			
				int slot = 0;
				for(UUID uuids : LobbyListener.participating) {
				Player participating = Bukkit.getPlayer(uuids);
				
				ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				SkullMeta sm = (SkullMeta) i.getItemMeta();
				String pname = participating.getDisplayName();
				sm.setDisplayName("§a"+pname);
				sm.setOwner(pname);
				i.setItemMeta(sm);
				
				inv.setItem(slot, i);
				slot = slot+1;
			}
			
			p.openInventory(inv);
			
		}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bKompass")) {
			e.setCancelled(true);
			
			String playername = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", "");
			Player participant = Bukkit.getPlayer(playername);
			if(participant.isOnline()) {
				e.getWhoClicked().teleport(participant);
				e.getWhoClicked().closeInventory();
			
			}
		}
	}

}
