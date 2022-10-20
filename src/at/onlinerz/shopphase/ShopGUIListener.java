package at.onlinerz.shopphase;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.tryjump.TryJump;

public class ShopGUIListener implements Listener {
	
	public String prefix = TryJump.prefix;
	
	public static File file = new File("plugins/TryJump/Shop/shopgui.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public String shopinvname = conf.getString("gui_name").replace("&", "§");
	

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(TryJump.phase.equalsIgnoreCase("shopping")) {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				if(p.getItemInHand().getType() == Material.CHEST) {
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 7, 7);
					OpenShopInventory.openGUI(p);
				}
				
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory().getName().equalsIgnoreCase(shopinvname)) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			
			if(e.getSlot() > 8) {
				// true; its an buyable item
				
				// turn Lore, which contains cost, into an Integer
				String costdescrip = e.getCurrentItem().getItemMeta().getLore().get(0);
				int itemcost = Integer.parseInt(costdescrip.substring(costdescrip.indexOf("§")+2, costdescrip.indexOf(" ")));
				int playerbalance = JumpListener.tokenbalance.get(p.getUniqueId());
				
				if(playerbalance >= itemcost) {
					int newbalance = playerbalance-itemcost;
					JumpListener.tokenbalance.replace(p.getUniqueId(), newbalance);
					ShopBoard.changeBalance(p, newbalance);
					p.playSound(p.getLocation(), Sound.WOOD_CLICK, 8, 8);
					
					if(e.getCurrentItem().getType() != Material.RED_ROSE) {
						if(e.getCurrentItem().getType() != Material.POTION) {
							p.getInventory().addItem(new ItemStack(e.getCurrentItem().getType()));
							
						} else {
							p.getInventory().addItem(new ItemStack(e.getCurrentItem().getType(), 1, e.getCurrentItem().getData().getData()));

						}
						
					} else {
						double newhp = p.getMaxHealth()+2;
						p.setMaxHealth(newhp);
						p.setHealth(newhp);
					}
					
					
				} else {
					p.sendMessage(prefix+"§cDu hast nicht genügend Tokens, um dieses Item zu kaufen!");
					p.playSound(p.getLocation(), Sound.BAT_HURT, 5, 5);
				}
				
			} else {
				// false; its a category item
				Material mat = e.getCurrentItem().getType();
				OpenShopInventory.updateCategory(p, mat);
			}
		}
		
	}
}
