package at.onlinerz.shopphase;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ShopBar {
	
	public static void sendActionbar() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {

				for(Player all : Bukkit.getServer().getOnlinePlayers()) {

					PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText("§6» §eAusrüsten"), (byte)2);
					((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				}
				
			}
		}, 0, 10);
	}

}
