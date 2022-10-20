package at.onlinerz.tryjump;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import at.onlinerz.deathmatch.DeathmatchListener;
import at.onlinerz.general.ClearJumpWorld;
import at.onlinerz.general.CompassListener;
import at.onlinerz.general.LoadSchematics;
import at.onlinerz.general.LoadWorlds;
import at.onlinerz.general.LocationsConfig;
import at.onlinerz.general.MainConfig;
import at.onlinerz.general.ModuleOrder;
import at.onlinerz.general.PrepareWorlds;
import at.onlinerz.general.PreventListener;
import at.onlinerz.general.SchematicFolders;
import at.onlinerz.general.SpectatorListener;
import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.lobbyphase.LobbyListener;
import at.onlinerz.lobbyphase.SetToRunningListener;
import at.onlinerz.shopphase.CMDskip;
import at.onlinerz.shopphase.EnchanterListener;
import at.onlinerz.shopphase.LoadShop;
import at.onlinerz.shopphase.ShopConfigFiles;
import at.onlinerz.shopphase.ShopGUIListener;

public class TryJump extends JavaPlugin {
	
	public static HashMap<Integer,LoadSchematics> modules = new HashMap<Integer,LoadSchematics>();
	
	public static String prefix;
	public static String joinmsg;
	public static String leavemsg;
	public static Plugin pl;
	public static Boolean running = false;
	public static String phase = "waiting";
	public static Boolean freeze = false;
	
	public void onEnable() {
		pl = this;
		
		LoadWorlds.searchWorlds();
		SchematicFolders.onCreation();
		MainConfig.Creation();
		LocationsConfig.Creation();
		ShopConfigFiles.onCreation();
		prefix = MainConfig.conf.getString("PluginPrefix").replace("&", "§");
		joinmsg = MainConfig.conf.getString("JoinMessage").replace("&", "§");
		leavemsg = MainConfig.conf.getString("LeaveMessage").replace("&", "§");
		
		ClearJumpWorld.deleteWorld();
		PrepareWorlds.createWorld();
		PrepareWorlds.generateSpawnpoints();
		PrepareWorlds.toSetRules();
		
		LoadSchematics.loadingSchematics();
		System.out.println(LoadSchematics.modules.size()+" Modul(e) wurde(n) erfolgreich geladen!");
		
		LoadShop.loadingCategories();
		System.out.println("Es wurden "+LoadShop.itemstobuy.size()+ " Items aus " +LoadShop.categories.size()+ " Kategorien erfolgreich geladen!");
		
		ModuleOrder.startDrawing();
		PrepareWorlds.generateFirstModul();
		
		this.getCommand("skip").setExecutor(new CMDskip());
		
		Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
		Bukkit.getPluginManager().registerEvents(new PreventListener(), this);
		Bukkit.getPluginManager().registerEvents(new JumpListener(), this);
		Bukkit.getPluginManager().registerEvents(new SetToRunningListener(), this);
		Bukkit.getPluginManager().registerEvents(new ShopGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new EnchanterListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathmatchListener(), this);
		Bukkit.getPluginManager().registerEvents(new SpectatorListener(), this);
		Bukkit.getPluginManager().registerEvents(new CompassListener(), this);
		
		System.out.println("[Tryjump] » Das Plugin wurde erfolgreich geladen");
	}
	

}
