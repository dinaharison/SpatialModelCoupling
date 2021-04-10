package ummisco.gama.spatialmodelcoupling.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msi.gama.kernel.simulation.SimulationAgent;
import msi.gama.kernel.simulation.SimulationClock;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.IConcept;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaDate;
import msi.gaml.skills.Skill;
import msi.gaml.species.ISpecies;
import msi.gaml.types.IType;
import ummisco.gama.spatialmodelcoupling.model.IModele;
import ummisco.gama.spatialmodelcoupling.types.ProcessDate;


@skill(name = IScheduleSkillV2.SKILL_NAME, concept = { IConcept.SKILL })
@vars({
	@variable (name = IScheduleSkillV2.CALENDAR, type = IType.LIST)
})
public class ScheduleSkillV2 extends Skill{
	
	@action(name = IScheduleSkillV2.INSERT_PROCESSES,
			args = {
					@arg (
							name = IScheduleSkillV2.PROCESS,
							type = IType.SPECIES,
							optional = false
							),
					@arg(
							name = IScheduleSkillV2.STEP,
							type = IType.FLOAT,
							optional = false
							)
			}
			)
	public void insertProcessList(IScope scope) {
		IAgent a = scope.getAgent();
		List<ProcessDate> calendar = new ArrayList<>();
		
		//get current calendar
		
		calendar = (List<ProcessDate>) a.getAttribute(IScheduleSkillV2.CALENDAR);
		//get the new process date inserted in the gama model
		
		ISpecies spec = (ISpecies) scope.getArg(IScheduleSkillV2.PROCESS, IType.SPECIES);
		double processStep = (Double) scope.getArg(IScheduleSkillV2.STEP, IType.FLOAT);
		
		ProcessDate processDate = new ProcessDate(spec,processStep);
		
		//if the process is already in the list replace it
		
		if(calendar.isEmpty()) {
			calendar.add(processDate);
		}else {
			calendar = ScheduleTools.calendarRedundantProcessCheck(calendar, processDate);
		}
		
		//refill the calendar with new values
		a.setAttribute(IScheduleSkillV2.CALENDAR, calendar);
		
	}
	
	@action(name = IScheduleSkillV2.SCHEDULE_FUNCTION)
	public void executeFirstCalendarEntry(IScope scope) {
		IAgent a = scope.getAgent();
		List<ProcessDate> calendar = (List<ProcessDate>) a.getAttribute(IScheduleSkillV2.CALENDAR);
		double currentTime = scope.getSimulation().getClock().getTimeElapsedInSeconds();
		
		List<ProcessDate> executedProcesses = ScheduleTools.getExecutedSpeciesAtCurrentStep(calendar, currentTime);
		
		while(!executedProcesses.isEmpty()) {
			
			executeProcesses(scope, executedProcesses);
			calendar = ScheduleTools.nextExecutionDateCalculation(scope,calendar, executedProcesses, currentTime);
			executedProcesses = ScheduleTools.getExecutedSpeciesAtCurrentStep(calendar, currentTime);
			
		}
		
		//execute agents in the list at the current time
		/* executeProcesses(scope, executedProcesses);
		
		//next execution date calculation
		calendar = ScheduleTools.nextExecutionDateCalculation(calendar, executedProcesses, currentTime);*/
		a.setAttribute(IScheduleSkillV2.CALENDAR, new ArrayList<ProcessDate>());
		a.setAttribute(IScheduleSkillV2.CALENDAR, calendar);
	}
	
	private void executeProcesses(IScope scope, List<ProcessDate> executionList) {
		
		Iterator<ProcessDate> I = executionList.iterator();
		
		//System.out.println(executionList);
		
		while(I.hasNext()) {
			ProcessDate p = I.next();
			ISpecies spec = p.getProcess();
			double pED = p.getNextExecutionDate();
			//int count = 0;
			//System.out.println("ScheduleSkillClass : date " + p.getProcess().getName() + " : "+pED);
			
			Iterator<IAgent> IagentList = 
			(Iterator<IAgent>) spec.getPopulation(scope).getAgents(scope).iterable(scope).iterator();
			
			//System.out.println((ArrayList<IAgent>) spec.getPopulation(scope).getAgents(scope));
			
			while(IagentList.hasNext()) {
				IAgent a = IagentList.next();
				a.setAttribute(IModele.EXECUTION, true);
				//count++;
				//a.setAttribute(IModele.NEXT_EXECUTION_DATE, pED);
			}
			
			//System.out.println("ScheduleSkillClass : species : " + p.getProcess() + " executed : " +count);
		}
		
	}
	
	@action(name = IScheduleSkillV2.PRINT_CALENDAR)
	public String printCalendar(IScope scope) {
		IAgent a = scope.getAgent();
		String s = "Cycle : " + scope.getSimulation().getClock().getCycle() + "\n" + 
					"Curent time (in Seconds : )" + scope.getSimulation().getClock().getTimeElapsedInSeconds() + "\n";
		
		List<ProcessDate> calendar = (List<ProcessDate>) a.getAttribute(IScheduleSkillV2.CALENDAR);
		Iterator<ProcessDate> I = calendar.iterator();
		
		while(I.hasNext()) {			
			s += I.next().toString();
		}
		return s;
	}
	
	
	
}
