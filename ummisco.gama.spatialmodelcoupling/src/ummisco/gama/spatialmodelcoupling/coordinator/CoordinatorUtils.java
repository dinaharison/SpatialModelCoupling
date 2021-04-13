package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ummisco.gama.spatialmodelcoupling.types.Modification;

public class CoordinatorUtils {
	
	public static LinkedList<Modification> sortModificationsByTime(List<Modification> modifications){
	    
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
	
	public static LinkedList<LinkedList<Modification>> groupByTime(List<Modification> mods){
		   LinkedList<LinkedList<Modification>> listFinal = new LinkedList();
		   
		   Comparator<Modification> compareByParam = (m1,m2) -> m1.getParameter().compareTo(m2.getParameter());
		   
		   while(!mods.isEmpty()){
		       Modification firstItem = mods.iterator().next();
		       LinkedList<Modification> toRemove = getSimultataneousModificaitons(firstItem,mods);
		       mods.removeAll(toRemove);
		       listFinal.add(toRemove.stream()
		            .sorted(compareByParam)
		            .collect(Collectors.toCollection(LinkedList::new))
		       );
		   }
		   
		   return listFinal;
		}
	
	//a modifier
	public static double evaluateModification(String parameter, Map<Modification,String> modifications, double parameterValue) {
		double evaluation = parameterValue;

		for(Map.Entry<Modification, String> entry:modifications.entrySet()) {
			if(parameter.equals(entry.getValue())) {
				evaluation += entry.getKey().value;
			}
		}

		return evaluation;
	}

	public static Map<Modification,String> evaluationList(String parameter, Map<Modification,String> modifications){
		Map<Modification,String> evaList = new HashMap<Modification,String>();

		for(Map.Entry<Modification, String> entry:modifications.entrySet()) {
			if(parameter.equals(entry.getValue())) {
				evaList.put(entry.getKey(), entry.getValue());
			}
		}

		return evaList;
	}

	public static List<String> getParameterList(Map<Modification,String> modificationList){

		List<String> paramList = new ArrayList<>();

		for(Map.Entry<Modification,String> entry:modificationList.entrySet()) {
			String parameter = entry.getValue();
			if(!paramList.contains(parameter)){
				paramList.add(parameter);
			}
		}

		return paramList;
	}

}
