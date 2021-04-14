package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

import msi.gaml.species.ISpecies;
import ummisco.gama.spatialmodelcoupling.types.Modification;

public class DefaultCoordinatorFuctions {
	
	public static LinkedList<Modification> fairModification(LinkedList<Modification> mods,double param){
		
		double total = 0;
		LinkedList<Modification> newList = new LinkedList<>();
		for ( Modification m : mods) {
			total += Math.abs(m.value); 
		}
		
		for (Modification m : mods) {
			double taux = (m.value*100)/total;
			double newValue = (taux*param)/100;
			Modification newMod = new Modification(m.agent, newValue, m.getParameter());
			newList.add(newMod);
		}
		
		return(newList);
	}
	
	public static LinkedList<Modification> filterUsingAgentAttribute(LinkedList<Modification> mods,String attr){
		Comparator<Modification> compareByAgentAttribute = (m1,m2) -> {
			
			String o1 = m1.agent.getAttribute(attr).toString();
			String o2 = m2.agent.getAttribute(attr).toString();
			
			return o1.compareTo(o2);
		};
		
		return new LinkedList<Modification>(
					
					mods.stream().sorted(compareByAgentAttribute).collect(Collectors.toCollection(LinkedList::new))
					
				);
	}
	
	public static LinkedList<Modification> extraspecificCompetition(LinkedList<Modification> mods,ISpecies spec) {
		Comparator<Modification> compareBySpecies = (m1,m2) -> {
			String specName = spec.getName();
			String n1 = m1.agent.getSpecies().getName();
			String n2 = m2.agent.getSpecies().getName();
			return specName.equals(n1) ? -1 :  specName.equals(n2) ? 1 : 0;
		};
		
		return new LinkedList<Modification>(
				
					mods.stream().sorted(compareBySpecies).collect(Collectors.toCollection(LinkedList::new))
				);
	}
	
	public static LinkedList<Modification> intraspecificCompetition(LinkedList<Modification> mods) {
		return filterUsingAgentAttribute(mods, "name");
	}
	

}
