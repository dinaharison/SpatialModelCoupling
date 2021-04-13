package ummisco.gama.spatialmodelcoupling.types;


import java.util.Iterator;
import java.util.List;

import msi.gaml.species.ISpecies;
import msi.gaml.statements.IStatement;

public class ModelRelation {

	//private String relationName;
	//private List<ISpecies> involvedModels;
	private String involvedParameter;
	private String coordinationMethod;
	
	public ModelRelation(String involvedParameter, String coordinationMethod) {
		super();
		//this.relationName = relationName;
		//this.involvedModels = involvedModels;
		
		this.involvedParameter = involvedParameter;
		this.coordinationMethod = coordinationMethod;
	}
	
	/*public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public List<ISpecies> getInvolvedModels() {
		return involvedModels;
	}
	public void setInvolvedModels(List<ISpecies> involvedModels) {
		this.involvedModels = involvedModels;
	} */
	
	public String getInvolvedParameter() {
		return involvedParameter;
	}
	public void setInvolvedParameter(String involvedParameter) {
		this.involvedParameter = involvedParameter;
	}
	public String getCoordinationMethod() {
		return coordinationMethod;
	}
	public void setCoordinationMethod(String coordinationMethod) {
		this.coordinationMethod = coordinationMethod;
	}
	
	public static String toString(ModelRelation mR) {
		String s ="";
		
		s = "Parameter : " + mR.involvedParameter + "\n" +
			"Coordination Method : " + mR.coordinationMethod + "\n";
		
		/*Iterator<ISpecies> I = mR.getInvolvedModels().iterator();
		while(I.hasNext()) {
			s += I.next().getName() + "\n"; 
		}	*/
		return s;
	}
}
