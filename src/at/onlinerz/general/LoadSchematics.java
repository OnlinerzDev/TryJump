package at.onlinerz.general;

import java.io.File;
import java.util.HashMap;

public class LoadSchematics {
	
	public static HashMap<Integer, SchematicData> modules = new HashMap<Integer, SchematicData>();
	
	public static File dir1 = new File("plugins/TryJump/Modules/Easy");
	public static File dir2 = new File("plugins/TryJump/Modules/Normal");
	public static File dir3 = new File("plugins/TryJump/Modules/Hard");
	public static File dir4 = new File("plugins/TryJump/Modules/Extreme");
	
	public static void loadingSchematics() {
		loadingSchematicsFromFiles(dir1, "Easy");
		loadingSchematicsFromFiles(dir2, "Normal");
		loadingSchematicsFromFiles(dir3, "Hard");
		loadingSchematicsFromFiles(dir4, "Extreme");
		
	}
	
	public static void loadingSchematicsFromFiles(File f, String crawlingDir) { 
		String[] fdirs = f.list();

		if(fdirs != null) { 
			for(String dirname : fdirs) {
				File moduleFile1 = new File(f.getAbsolutePath()+"/"+dirname+"/module.schematic");
				File moduleFile2 = new File(f.getAbsolutePath()+"/"+dirname+"/simplified.schematic");
				if(moduleFile1.exists()) {
					String moduleName = dirname;
					String difficulty = crawlingDir;
				
					if(moduleFile2.exists()) {
						modules.put(getid(), new SchematicData(moduleFile1, moduleName, difficulty, moduleFile2));
						System.out.println("Modul '" +moduleName+ "' mit Schwierigkeitsgrad " +difficulty+ " wurde mit seinem vereinfachtem Modul geladen");
					
					} else {
						modules.put(getid(), new SchematicData(moduleFile1, moduleName, difficulty, null));
						System.out.println("Modul '" +moduleName+ "' mit Schwierigkeitsgrad " +difficulty+ " ohne vereinfachtem Modul wurde geladen");
				}
			}
			
		}
		}
	}
	
	public static Integer getid() {
		if(modules.isEmpty()) {
			return 1;
		} else {
			return modules.size()+1;
		}
	}

}
