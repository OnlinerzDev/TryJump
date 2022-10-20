package at.onlinerz.general;

import java.io.File;

public class SchematicData {
	
	private File schematicFile;
	private String modulename;
	private String difficulty;
	private File simplifiedschematicFile;
		
	public SchematicData(File schematicFile, String modulename, String difficulty, File simplifiedschematicFile) {
		this.setSchematicFile(schematicFile);
		this.setModulename(modulename);
		this.setDifficulty(difficulty);
		this.setSimplifiedschematicFile(simplifiedschematicFile);
	}

	public File getSchematicFile() {
		return schematicFile;
	}

	public void setSchematicFile(File schematicFile) {
		this.schematicFile = schematicFile;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public File getSimplifiedschematicFile() {
		return simplifiedschematicFile;
	}

	public void setSimplifiedschematicFile(File simplifiedschematicFile) {
		this.simplifiedschematicFile = simplifiedschematicFile;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	
	

}
