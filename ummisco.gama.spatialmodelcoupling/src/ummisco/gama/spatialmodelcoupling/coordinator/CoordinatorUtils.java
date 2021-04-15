package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ummisco.gama.spatialmodelcoupling.types.ModelRelation;
import ummisco.gama.spatialmodelcoupling.types.Modification;

public class CoordinatorUtils {
	
	public static LinkedList<Modification> sortModificationsByTime(List<Modification> modifications){  ///first
	    
	    return new LinkedList<Modification> (
	        modifications.stream()
	        .sorted(Comparator.comparingDouble(Modification::getExecutedTime))
	        .collect(
	            Collectors.toCollection(LinkedList::new)
	            )
	         );
	}
	
	public static LinkedList<Modification> getSimultataneousModificaitons(Modification mod, List<Modification> mods){
	    double time = mod.getExecutedTime();
	    return new LinkedList<Modification> (
	        mods.stream()
	        .filter(item -> item.getExecutedTime() == time)
	        .collect(
	            Collectors.toCollection(LinkedList::new)
	        )
	        );
	}
	
	public static LinkedList<LinkedList<Modification>> groupByTime(List<Modification> mods){ ///second
		   LinkedList<LinkedList<Modification>> listFinal = new LinkedList();
		   
		   Comparator<Modification> compareByParam = (m1,m2) -> m1.getParameter().compareTo(m2.getParameter());
		   
		   while(!mods.isEmpty()){
		       Modification firstItem = mods.iterator().next();
		       LinkedList<Modification> toRemove = getSimultataneousModificaitons(firstItem,mods);
		       mods.removeAll(toRemove);
		       
		       while(!toRemove.isEmpty()){
		           Modification mod = toRemove.iterator().next();
		           LinkedList<Modification> toRemoveTemp = toRemove.stream()
		            .filter(m -> m.getParameter().equals(mod.getParameter()))
		            .collect(Collectors.toCollection(LinkedList::new));
		           
		           listFinal.add(toRemoveTemp);
		           toRemove.removeAll(toRemoveTemp);
		       }
		   }
		   
		   return listFinal;
	}
	
	//a modifier
	public static double evaluateModification(String parameter, LinkedList<Modification> mods, double parameterValue) {
		double evaluation = parameterValue;

		for (Modification mod : mods) {
			
			evaluation += mod.value;
			
		}

		return evaluation;
	}
	
	public static double noRessource(String parameter, LinkedList<Modification> mods, double parameterValue) {
		double evaluation = parameterValue;

		for (Modification mod : mods) {
			if(mod.value>0) {
				evaluation += mod.value;
			}
		}

		return evaluation;
	}
	
	public static ModelRelation getModelInteraction(String param, List<ModelRelation> mRL) {
		return mRL.stream().filter((m)->m.getInvolvedParameter().equals(param)).findFirst().get();
	}
	
	public static void printModificationList(LinkedList<LinkedList<Modification>> mods) {
		
		mods.stream().forEach(				
				item -> {
					System.out.println("*****************************************");
					item.stream().forEach(							
							e -> System.out.println(e.toString())							
							);
				}				
				);		
	}

}
