package at.onlinerz.jumpphase;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;

import at.onlinerz.general.LoadSchematics;
import at.onlinerz.tryjump.TryJump;

@SuppressWarnings("deprecation")
public class GenerateSchematics {
	
	public static void generateNext(Location loc, int index) {
		
		try {
			File dir = LoadSchematics.modules.get(index).getSchematicFile();

			EditSession editSession = new EditSession(new BukkitWorld(loc.getWorld()), 999999999);
			editSession.enableQueue();

			SchematicFormat schematic = SchematicFormat.getFormat(dir);
			CuboidClipboard clipboard = schematic.load(dir);

				clipboard.paste(editSession, BukkitUtil.toVector(loc), true);
				editSession.flushQueue();
		} catch (DataException | IOException ex) {
			ex.printStackTrace();
		} catch (MaxChangedBlocksException ex) {
			ex.printStackTrace();
		}
		
		loc.setY(loc.getY()-1);
		Material m = loc.getBlock().getType();
		loc.getBlock().setType(m);
		

		Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.pl, new Runnable() {
			
			@Override
			public void run() {
				loc.getBlock().setType(Material.AIR);
				
			}
		}, 10);
		
		
	}
	
	public static void generateSimplified(Location loc, int id) {
		
		try {
			File dir = LoadSchematics.modules.get(id).getSimplifiedschematicFile();

			EditSession editSession = new EditSession(new BukkitWorld(loc.getWorld()), 999999999);
			editSession.enableQueue();

			SchematicFormat schematic = SchematicFormat.getFormat(dir);
			CuboidClipboard clipboard = schematic.load(dir);

				clipboard.paste(editSession, BukkitUtil.toVector(loc), true);
				editSession.flushQueue();
		} catch (DataException | IOException ex) {
			ex.printStackTrace();
		} catch (MaxChangedBlocksException ex) {
			ex.printStackTrace();
		}
	}

}
