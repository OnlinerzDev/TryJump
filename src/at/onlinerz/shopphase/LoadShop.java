package at.onlinerz.shopphase;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class LoadShop {
	
	public static File file = new File("plugins/TryJump/Shop/shopgui.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

	public static HashMap<String, ShopData> categories = new HashMap<String, ShopData>();
	public static HashMap<String, ShopData> itemstobuy = new HashMap<String, ShopData>();
	public static HashMap<String, Short> potions = new HashMap<String, Short>();
	
	public static void loadingCategories() {
		
		for(String sections : conf.getKeys(false)) {
			if(! sections.equalsIgnoreCase("gui_name")) {
				String displayname = conf.getString(sections+".displayname").replace("&", "§");
				Material material = Material.matchMaterial(conf.getString(sections+".material"));
				int slot = conf.getInt(sections+".slot");
			
				// cost of 0 tells the plugin by loading the GUI its not to buy, its a category-item, which will lead to a subgui
				// (you could also seperate category-items and buyable items into two DataClasses, but the difference is the costs)
				categories.put(sections, new ShopData(displayname, material, slot, 0));
			
				String keyPath = conf.getString(sections+".filepath");
				// now put the items to buy of that category into the 'itemstobuy' hashmap, with the section key to assign it to the category
				loadingBuyableItems(keyPath, sections);
			
			}
			
		}
			
		}
	
	public static void loadingBuyableItems(String keyPath, String categoryKey) {
		File subgui = new File(keyPath);
		YamlConfiguration subconf = YamlConfiguration.loadConfiguration(subgui);
		
		for(String sections : subconf.getKeys(false)) {
			String displayname = subconf.getString(sections+".displayname").replace("&", "§");
			Material material = Material.matchMaterial(subconf.getString(sections+".material"));
			int slot = subconf.getInt(sections+".slot");
			int cost = subconf.getInt(sections+".cost");
			
			itemstobuy.put(categoryKey+"_"+slot, new ShopData(displayname, material, slot, cost));
			
			if(material == Material.POTION) {
				potions.put(categoryKey+"_"+slot, (short) subconf.getInt(sections+".potionid"));
				
			}
	
		}
	}

}
