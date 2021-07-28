package ummisco.gama.spatialmodelcoupling.types;


import java.util.Iterator;
import java.util.List;

import msi.gaml.species.ISpecies;
import msi.gaml.statements.IStatement;

public class ModelRelation {

	//private String relationName;
	//private List<ISpecies> involvedModels;
	private String involvedParameter;	
	private String agentAttr;
	private boolean intra_comp;
	private boolean extra_comp;
	private boolean fair_dist;
	private ISpecies dom_spec;

	public ModelRelation(String involvedParameter, String agentAttr, boolean intra_comp,
			boolean extra_comp, boolean fair_dist, ISpecies dom_spec) {
		super();
		this.involvedParameter = involvedParameter;
		this.agentAttr = agentAttr;
		this.intra_comp = intra_comp;
		this.extra_comp = extra_comp;
		this.fair_dist = fair_dist;
		this.dom_spec = dom_spec;
	}
	
	public ISpecies getDom_spec() {
		return dom_spec;
	}

	public void setDom_spec(ISpecies dom_spec) {
		this.dom_spec = dom_spec;
	}
	
	public String getInvolvedParameter() {
		return involvedParameter;
	}
	public void setInvolvedParameter(String involvedParameter) {
		this.involvedParameter = involvedParameter;
	}
	
	public String getAgentAttr() {
		return agentAttr;
	}

	public void setAgentAttr(String agentAttr) {
		this.agentAttr = agentAttr;
	}

	public boolean isIntra_comp() {
		return intra_comp;
	}

	public void setIntra_comp(boolean intra_comp) {
		this.intra_comp = intra_comp;
	}

	public boolean isExtra_comp() {
		return extra_comp;
	}

	public void setExtra_comp(boolean extra_comp) {
		this.extra_comp = extra_comp;
	}

	public boolean isFair_dist() {
		return fair_dist;
	}

	public void setFair_dist(boolean fair_dist) {
		this.fair_dist = fair_dist;
	}
	
	public static String toString(ModelRelation mR) {
		String s ="";
		
		s = "Parameter : " + mR.involvedParameter + "\n" + 
			"intraspecific competition : " + mR.intra_comp + "\n" + 
			"extraspecific competition : " + mR.extra_comp + " Dominant species :"+ mR.dom_spec + "\n" + 
			"fair distribution : " + mR.fair_dist + "\n";

		return s;
	}
	
	public  String toString() {
		String s ="";
		
		s = "Parameter : " + this.involvedParameter + "\n" + 
			"intraspecific competition : " + this.intra_comp + "\n" + 
			"extraspecific competition : " + this.extra_comp + " Dominant species :"+ this.dom_spec + "\n" + 
			"fair distribution : " + this.fair_dist + "\n";

		return s;
	}
}
