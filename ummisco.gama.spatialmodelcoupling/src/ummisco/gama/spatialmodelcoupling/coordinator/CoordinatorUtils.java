package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ummisco.gama.spatialmodelcoupling.types.Modification;

public class CoordinatorUtils {
	
	public static LinkedHashMap<Modification,String> sortModificationsByTime(Map<Modification,String> modifications){
	    
	    Comparator<Modification> timeComparator = (mod1,mod2)-> mod1.getExecutedTime().compareTo(mod2.getExecutedTime());
	    
	    LinkedHashMap<Modification,String> modsByTime = modifications.entrySet()
	        .stream()
	        .sorted( Map.Entry.comparingByKey(timeComparator))
	        .collect(
	            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                 (oldValue, newValue) -> oldValue, LinkedHashMap::new)
	            );
	            
	    return modsByTime;
	    
	}
	
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
