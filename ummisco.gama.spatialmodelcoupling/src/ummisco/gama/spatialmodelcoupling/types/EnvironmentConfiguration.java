package ummisco.gama.spatialmodelcoupling.types;
import java.util.List;

import msi.gaml.species.ISpecies;


public class EnvironmentConfiguration {
	
	public String scenarioName;
	public List<ISpecies> SpeciesList;
	public String resolverMethod;
	
	public EnvironmentConfiguration(String scenarioName, List<ISpecies> speciesList, String resolverMethod) {
		super();
		this.scenarioName = scenarioName;
		SpeciesList = speciesList;
		this.resolverMethod = resolverMethod;
	}	
}
