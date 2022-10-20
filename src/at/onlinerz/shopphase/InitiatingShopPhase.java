package at.onlinerz.shopphase;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;

import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.tryjump.TryJump;

public class InitiatingShopPhase {
	
	public static File file = new File("plugins/TryJump/Locations/lobbies.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static void initiatingShopphase() {
		
		TryJump.phase = "shopping";
		
		ItemStack shopitem = new ItemStack(Material.CHEST);
		ItemMeta shopim = shopitem.getItemMeta();
		shopim.setDisplayName("§bShop");
		shopitem.setItemMeta(shopim);
		
		Location spawn = (Location) conf.get("purchaselobby"); 
		ShopBar.sendActionbar();
		ShopBoard.Timer();
		
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			if(LobbyListener.participating.contains(all.getUniqueId())) {
				all.playSound(all.getLocation(), Sound.LEVEL_UP, 10, 10);
				all.getInventory().setItem(4, shopitem);
			
			}
			
				all.teleport(spawn);
				all.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				ShopBoard.BaseBoard(all);
			
			}
		}

}
