package at.onlinerz.shopphase;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenShopInventory {
	
	public static File file = new File("plugins/TryJump/Shop/shopgui.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static void openGUI(Player p) {
		String invname = conf.getString("gui_name").replace("&", "§");
		Inventory inv = Bukkit.createInventory(null, 2*9, invname);
		
		Map<String, ShopData> categorymap = LoadShop.categories;
		for(Map.Entry<String, ShopData> e : categorymap.entrySet()) {
			
			ItemStack i = new ItemStack(e.getValue().getMaterial());
			ItemMeta im = i.getItemMeta();
			im.setDisplayName(e.getValue().getDisplayname());
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			i.setItemMeta(im);
			
			inv.setItem(e.getValue().getSlot(), i);
			
			
			
		}
		
		p.openInventory(inv);
	}
	
	public static void updateCategory(Player p, Material material) {
		
		p.playSound(p.getLocation(), Sound.CLICK, 7, 7);
		
		// clear current categoryline
		for(int i= 9; i < 18; i++) {
			p.getOpenInventory().setItem(i, new ItemStack(Material.AIR));
		}
		
		for(Map.Entry<String, ShopData> e : LoadShop.categories.entrySet()) {
			Material categMaterial = e.getValue().getMaterial();
			
			if(material == categMaterial) {
				String categKey = e.getKey();
				
				for(Map.Entry<String, ShopData> e2 : LoadShop.itemstobuy.entrySet()) {
					if(e2.getKey().contains(categKey)) {
						
						if(e2.getValue().getMaterial() != Material.POTION) {
							ItemStack i = new ItemStack(e2.getValue().getMaterial());
							ItemMeta im = i.getItemMeta();
							im.setDisplayName(e2.getValue().getDisplayname());
							ArrayList<String> lore = new ArrayList<String>();
							lore.add("§6"+e2.getValue().getCost()+" Tokens");
							im.setLore(lore);
							i.setItemMeta(im);
							p.getOpenInventory().setItem(e2.getValue().getSlot(), i);
							
						} else {
							short potionid = LoadShop.potions.get(e2.getKey());
							ItemStack i = new ItemStack(e2.getValue().getMaterial(), 1, potionid);
							ItemMeta im = i.getItemMeta();
							im.setDisplayName(e2.getValue().getDisplayname());
							ArrayList<String> lore = new ArrayList<String>();
							lore.add("§6"+e2.getValue().getCost()+" Tokens");
							im.setLore(lore);
							i.setItemMeta(im);
							p.getOpenInventory().setItem(e2.getValue().getSlot(), i);
						}
						
					}
				}
			}
		}
	}
	

}
