package at.onlinerz.jumpphase;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.onlinerz.general.LoadSchematics;
import at.onlinerz.general.ModuleOrder;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class JumpBar {
	
	public static void TimerTask() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(JumpListener.currentModule.containsKey(p.getUniqueId())) {
						int currentModule = JumpListener.currentModule.get(p.getUniqueId());
						int modulenumber = ModuleOrder.moduleorder.get(currentModule-1);
						String moduleName = LoadSchematics.modules.get(modulenumber).getModulename();
						String difficulty = LoadSchematics.modules.get(modulenumber).getDifficulty();
						String DisplayText = getdifficultyDisplay(difficulty);
						PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText("§6» §aUnit " +currentModule+ ": §e" +moduleName+ "§6 | " +DisplayText+ " §6«"), (byte)2);
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		            
					}

			}
				
			}
		}, 0, 10);

		}
	
	public static String getdifficultyDisplay(String difficulty) {
		
		switch(difficulty) {
		case "Easy":
			return "§aEASY";
		case "Normal":
			return "§6NORMAL";
		case "Hard":
			return "§cHARD";
		case "Extreme":
			return "§5EXTREME";
		default:
			return "§aEASY";
			
		}
		
	}
}
