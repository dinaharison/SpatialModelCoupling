package ummisco.gama.spatialmodelcoupling.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;
import msi.gaml.species.ISpecies;
import ummisco.gama.spatialmodelcoupling.types.ProcessDate;

public class ScheduleTools {
	
	public static List<ProcessDate> calendarRedundantProcessCheck(List<ProcessDate> calendar, ProcessDate p) {
		
		Iterator<ProcessDate> I = calendar.iterator();
		ProcessDate oldProcess = null;
		
		while(I.hasNext()) {
			ProcessDate pD = I.next();
			if(isSameProcessDate(p, pD)) {
				oldProcess = pD;
				break;
			}
		}
		
		if(oldProcess==null) {
			calendar.add(p);
		}else {
			calendar.remove(oldProcess);
			calendar.add(p);
		}
		
		return sortCalendar(calendar);
	}
	
	
	public static List<ProcessDate> getExecutedSpeciesAtCurrentStep(List<ProcessDate> calendar, double currentTime){

		List<ProcessDate> executedSpecies = calendar.stream()
				.filter(p -> p.getNextExecutionDate()<=currentTime)
				.collect(Collectors.toList());
		
		return executedSpecies;
		
	}
	
	public static List<ProcessDate> nextExecutionDateCalculation(IScope scope,List<ProcessDate> calendar, List<ProcessDate> executedSpecies, double currentTime){
		
		Iterator<ProcessDate> Iexecuted = executedSpecies.iterator();
		
		while(Iexecuted.hasNext()) {
			ProcessDate p = Iexecuted.next();
			calendar.remove(p);
			p.proceedToNextDate();
			p.updateAgentStep(scope);
			calendar.add(p);		
		}
		
		return sortCalendar(calendar);
		
	}
	
	
	public static List<ProcessDate> sortCalendar(List<ProcessDate> calendar){
		
		List<ProcessDate> sortedcalendar = calendar;
		
		Comparator<ProcessDate> compareByDate = (ProcessDate p1, ProcessDate p2) -> 
			doubleCompareTo(p1.getNextExecutionDate(),p2.getNextExecutionDate());
		
		Collections.sort(sortedcalendar,compareByDate);
		return sortedcalendar;
	}
	
	private static int doubleCompareTo(double d1, double d2) {
		if(d1<d2) {return -1;}
		else return 0;
	}
	
	public static boolean isSameProcessDate(ProcessDate p1, ProcessDate p2) {
		
		return p1.getProcess().getName().equals(p2.getProcess().getName());
		
	}
	
}
