package at.onlinerz.shopphase;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class ShopConfigFiles {
	
	public static File file = new File("plugins/TryJump/Shop/shopgui.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static File file2 = new File("plugins/TryJump/Shop/SubGUIs/weapons.yml");
	public static File file3 = new File("plugins/TryJump/Shop/SubGUIs/armor1.yml");
	public static File file4 = new File("plugins/TryJump/Shop/SubGUIs/armor2.yml");
	public static File file5 = new File("plugins/TryJump/Shop/SubGUIs/armor3.yml");
	public static File file6 = new File("plugins/TryJump/Shop/SubGUIs/food.yml");
	public static File file7 = new File("plugins/TryJump/Shop/SubGUIs/potions.yml");
	public static File file8 = new File("plugins/TryJump/Shop/SubGUIs/specials.yml");
	
	public static void onCreation() {
		if(! file.exists()) {

			conf.set("gui_name", "&6» &l&eShop");
			
			conf.set("subgui1.displayname", "&bWaffen");
			conf.set("subgui1.material", Material.DIAMOND_SWORD.name());
			conf.set("subgui1.slot", 0);
			conf.set("subgui1.filepath", file2.getPath());
			
			conf.set("subgui2.displayname", "&bKettenrüstung");
			conf.set("subgui2.material", Material.CHAINMAIL_CHESTPLATE.name());
			conf.set("subgui2.slot", 2);
			conf.set("subgui2.filepath", file3.getPath());
			
			conf.set("subgui3.displayname", "&bEisenrüstung");
			conf.set("subgui3.material", Material.IRON_CHESTPLATE.name());
			conf.set("subgui3.slot", 3);
			conf.set("subgui3.filepath", file4.getPath());
			
			conf.set("subgui4.displayname", "&bDiamantrüstung");
			conf.set("subgui4.material", Material.DIAMOND_CHESTPLATE.name());
			conf.set("subgui4.slot", 4);
			conf.set("subgui4.filepath", file5.getPath());
			
			conf.set("subgui5.displayname", "&bNahrung");
			conf.set("subgui5.material", Material.CAKE.name());
			conf.set("subgui5.slot", 6);
			conf.set("subgui5.filepath", file6.getPath());
			
			conf.set("subgui6.displayname", "&bTränke");
			conf.set("subgui6.material", Material.POTION.name());
			conf.set("subgui6.slot", 7);
			conf.set("subgui6.filepath", file7.getPath());
			
			conf.set("subgui7.displayname", "&bSpezial");
			conf.set("subgui7.material", Material.EMERALD.name());
			conf.set("subgui7.slot", 8);
			conf.set("subgui7.filepath", file8.getPath());
			
			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			setExampleItem(file2);
			setExampleItem(file3);
			setExampleItem(file4);
			setExampleItem(file5);
			setExampleItem(file6);		
			setExampleItem(file7);
			setExampleItem(file8);
			
		}
	
		
		
	}
	
	public static void setExampleItem(File f) {
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		if(! f.equals(file8)) {
			if(! f.equals(file7)) {
				config.set("item1.displayname", "&aItemname");
				config.set("item1.material", Material.BARRIER.name());
				config.set("item1.cost", 100);
				config.set("item1.slot", 9);
				
			} else {
				config.set("potion1.displayname", "&dTrank der Heilung");
				config.set("potion1.material", Material.POTION.name());
				config.set("potion1.potionid", 8197);
				config.set("potion1.cost", 250);
				config.set("potion1.slot", 9);
			}
		
		} else {
			config.set("item1.displayname", "&cExtra Herz");
			config.set("item1.material", Material.RED_ROSE.name());
			config.set("item1.cost", 200);
			config.set("item1.slot", 13);
		}
		
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
