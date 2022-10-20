package at.onlinerz.lobbyphase;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import at.onlinerz.tryjump.TryJump;

public class SetToRunningListener implements Listener {
	
	
	@EventHandler
	public void on(ServerListPingEvent e) {
		if(! (TryJump.phase.equalsIgnoreCase("waiting"))) {
	        e.setMotd("Playing");
	        
		}
	    }

}
