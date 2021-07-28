package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.List;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;
import msi.gama.util.graph.writer.AvailableGraphWriters;
import msi.gaml.operators.Cast;
import msi.gaml.species.ISpecies;
import ummisco.gama.spatialmodelcoupling.model.IModele;
import ummisco.gama.spatialmodelcoupling.types.Modification;

public class DefaultCoordinatorFuctions {
	
	public static LinkedList<Modification> fairModification(LinkedList<Modification> mods,double param){
		double eva =0;
		double total = 0;
		LinkedList<Modification> newList = new LinkedList<>();
		for ( Modification m : mods) {
			if(m.value<0) {
				total += Math.abs(CoordinatorUtils.round(m.value, 3));
			}
		}
		
		total = CoordinatorUtils.round(total, 3);
		
		for (Modification m : mods) {
			if(m.value<0) {
				double taux = (Math.abs(CoordinatorUtils.round(m.value, 3))*100)/total;
				//0.01% pour la précision, peut donner une valeur négative si la ressource est exploité à 100% 
				double newValue = ((taux-0.01)*param)/100;
				
				newValue = CoordinatorUtils.round(newValue, 3);
				
				Modification newMod = new Modification(m.agent, -newValue, m.getParameter());
				newMod.addModificationToAgent(newMod.agent);
				newList.add(newMod);
				
				eva += -newValue;
			}else {
				newList.add(m);
				eva += m.value;
			}
		}
		System.out.println("parameter : " + param + " total : " + total + " eva : " + eva);
		return(newList);
	} /// ok 
	
	public static LinkedList<Modification> filterUsingAgentAttribute(LinkedList<Modification> mods,String attr, IScope scope, double avalaibleRessource){
		Comparator<Modification> compareByAgentAttribute = (m1,m2) -> {
			
			String o1 = m1.agent.getAttribute(attr).toString();
			String o2 =m2.agent.getAttribute(attr).toString();
			
			return o1.compareTo(o2);
		};
		
		LinkedList<Modification> newList = mods.stream().sorted(compareByAgentAttribute).collect(Collectors.toCollection(LinkedList::new));
		LinkedList<Modification> definitiveList = getEligibleAgentForModification(newList,avalaibleRessource);
		
		definitiveList.forEach(m -> {
			
			m.addModificationToAgent(m.agent);
			
		});
		
		return definitiveList;
	}
	
	public static LinkedList<Modification> extraspecificCompetition(LinkedList<Modification> mods,ISpecies spec, double avalaibleRessource) {
		Comparator<Modification> compareBySpecies = (m1,m2) -> {
			String specName = spec.getName();
			String n1 = m1.agent.getSpecies().getName();
			String n2 = m2.agent.getSpecies().getName();
			return specName.equals(n1) ? -1 :  specName.equals(n2) ? 1 : 0;
		};
		
		LinkedList<Modification> newList = mods.stream().sorted(compareBySpecies).collect(Collectors.toCollection(LinkedList::new));
		
		LinkedList<Modification> definitiveList =getEligibleAgentForModification(newList,avalaibleRessource);
		
		definitiveList.forEach(m -> {
			
			m.addModificationToAgent(m.agent);
			
		});
		
		return definitiveList;
	}
	
	public static LinkedList<Modification> intraspecificCompetition(LinkedList<Modification> mods,IScope scope, double avalaibleRessource) {
		Comparator<Modification> compareByAgentName = (m1,m2) -> {
			
			String o1 = m1.agent.getName();
			String o2 =m2.agent.getName();
			
			return o1.compareTo(o2);
		};
		
		LinkedList<Modification> newList = mods.stream().sorted(compareByAgentName).collect(Collectors.toCollection(LinkedList::new));
		
		LinkedList<Modification> definitiveList = getEligibleAgentForModification(newList,avalaibleRessource);
		
		definitiveList.forEach(m -> {
			
			m.addModificationToAgent(m.agent);
			
		});
		
		return definitiveList;
	}
	
	
	public static LinkedList<Modification> getEligibleAgentForModification(LinkedList<Modification> mods, double res) {
		
		LinkedList<Modification> newList = new LinkedList<Modification>();
		double newValue = res;
		
		for (Modification modification : mods) {
			newValue = newValue + modification.value;
			if(newValue<0) break;
			else {
				modification.addModificationToAgent(modification.agent);
				newList.add(modification);
			}
		}
		
		return newList;
	}

}
