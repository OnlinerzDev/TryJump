package at.onlinerz.general;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

public class ClearJumpWorld {
	
	public static void deleteWorld() {
		Bukkit.unloadWorld(Bukkit.getWorld("jumpworld"), false);

				try {
					FileUtils.deleteDirectory(new File("jumpworld"));
				} catch (IOException e) {
					e.printStackTrace();

				}
				
	}

}
