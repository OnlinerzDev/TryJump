package at.onlinerz.general;

import java.io.File;

public class SchematicFolders {
	
	public static File file1 = new File("plugins/TryJump/Modules/Easy/ExampleModul");
	public static File file2 = new File("plugins/TryJump/Modules/Normal/ExampleModul");
	public static File file3 = new File("plugins/TryJump/Modules/Hard/ExampleModul");
	public static File file4 = new File("plugins/TryJump/Modules/Extreme/ExampleModul");
	
	public static void onCreation() {
		if(! file1.exists()) {
			file1.mkdirs();
		}
		
		if(! file2.exists()) {
			file2.mkdirs();
		}
		
		if(! file3.exists()) {
			file3.mkdirs();
		}
		
		if(! file4.exists()) {
			file4.mkdirs();
		}
	}

}
