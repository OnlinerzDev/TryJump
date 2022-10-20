package at.onlinerz.general;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig {

	public static File file = new File("plugins/TryJump/config.yml");
	public static YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static void Creation() {
		if(! file.exists()) {
			conf.set("PluginPrefix", "&6» &e&lTryjump §7| ");
			conf.set("ServerName", "DeinServer.net");
			conf.set("JoinMessage", "&7[&a+&7] &7%player%");
			conf.set("LeaveMessage", "&7[&c-&7] &7%player%");
			conf.set("PlayersToStart", 2);
			conf.set("MaxPlayerAmount", 10);
			conf.set("SpawnPointDistance", 50);
			conf.set("SpawnPointYCoordinate", 10);
			
			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
