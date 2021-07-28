package ummisco.gama.spatialmodelcoupling.coordinator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import ummisco.gama.spatialmodelcoupling.types.Modification;

public class ResultFileUpdate {
	private static String CURRENT_TIME = "Current Time";
	private static String AGENTNAME = "Agent";
	private static String PARAM = "Ressource";
	private static String VALUE = "Modified Value";
	private static String EXE = "Execution time";
	private static String SEPARATOR = ";";
	
	public static FileWriter writeFile(LinkedList<Modification> mods, String path) throws IOException {
		 FileWriter file = new FileWriter(path);
		 file.append(CURRENT_TIME);
		 file.append(SEPARATOR);
		 
		 file.append(AGENTNAME);
		 file.append(SEPARATOR);
		 
		 file.append(PARAM);
		 file.append(SEPARATOR);
		 
		 file.append(VALUE);
		 file.append(SEPARATOR);
		 
		 file.append(EXE);
		 file.append(SEPARATOR);
		 
		 mods.forEach(m -> {
			 
			 
			 
		 });
		 
		 
		 file.flush();
		 file.close();
		 return file;
	}
	
	
}
