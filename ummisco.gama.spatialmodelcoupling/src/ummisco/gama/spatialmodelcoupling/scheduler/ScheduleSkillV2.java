package ummisco.gama.spatialmodelcoupling.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
import msi.gama.runtime.concurrent.GamaExecutorService;
import msi.gama.util.GamaDate;
import msi.gaml.operators.Cast;
import msi.gaml.skills.Skill;
import msi.gaml.species.ISpecies;
import msi.gaml.statements.DoStatement;
import msi.gaml.statements.IExecutable;
import msi.gaml.statements.IStatement;
import msi.gaml.types.IType;
import ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2;
import ummisco.gama.spatialmodelcoupling.coordinator.CoordinatorUtils;
import ummisco.gama.spatialmodelcoupling.coordinator.DefaultCoordinatorFuctions;
import ummisco.gama.spatialmodelcoupling.coordinator.IConflictResolverSkill;
import ummisco.gama.spatialmodelcoupling.model.IModele;
import ummisco.gama.spatialmodelcoupling.types.ModelRelation;
import ummisco.gama.spatialmodelcoupling.types.Modification;
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
			List<IAgent> particlesScheduled = executeProcesses(scope, executedProcesses);
			
			
			particlesScheduled.forEach(agt -> {
				modifications(agt);
			});
			
			calendar = ScheduleTools.nextExecutionDateCalculation(scope,calendar, executedProcesses, currentTime);
			executedProcesses = ScheduleTools.getExecutedSpeciesAtCurrentStep(calendar, currentTime);
			
		}

		a.setAttribute(IScheduleSkillV2.CALENDAR, new ArrayList<ProcessDate>());
		a.setAttribute(IScheduleSkillV2.CALENDAR, calendar);
		
	}
	
	private List<IAgent> executeProcesses(IScope scope, List<ProcessDate> executionList) {
		
		Iterator<ProcessDate> I = executionList.iterator();
		List<IAgent> particleList = new ArrayList<>();
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
				a.setAttribute(IModele.EXPLOITED_RESSOURCES, new ArrayList<>());
				a.step(scope);				
				particleList.add((IAgent) a.getAttribute(IModele.SPACE_PARTICLE));
				a.setAttribute(IModele.EXECUTION, false);
			}
		}
	   
		return particleList.stream().distinct().collect(Collectors.toList());
	}
	
	public void checkConflictInSpaceParticle(IScope scope) {
		
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
	
	public static void modifications(IAgent a) {
		List<Modification> mods = (List<Modification>)a.getAttribute(IConflictResolverSkill.MODIFICATION_LIST);
		
		//System.out.println(a.getName());
		
		LinkedList<Modification> modSortedByParameters = CoordinatorUtils.sortModificationsByParameter(mods);
		
		
		while(!modSortedByParameters.isEmpty()) {
			//get the first element
			Modification firstMod = modSortedByParameters.getFirst();
			String p = firstMod.getParameter();
			//collect all modifications on the same parameter as the first item
			
			LinkedList<Modification> evaList = modSortedByParameters.stream().filter(
					
					m -> m.getParameter().equals(firstMod.getParameter())
					
					).collect(Collectors.toCollection(LinkedList::new));
			
			//apply the corresponding coordination function
			
			List<ModelRelation> mRL = (List<ModelRelation>) a.getAttribute(IConflictResolverSkill.MODEL_INTERACTION);
			
			ModelRelation mR = CoordinatorUtils.getModelInteraction(p, mRL);
			LinkedList<Modification> newEvaList = new LinkedList<>();
			
			double avalaibleRessource = Cast.asFloat(a.getScope(), a.getAttribute(p));
			
			avalaibleRessource = CoordinatorUtils.round(avalaibleRessource, 3);
			
			if(avalaibleRessource>0) {
				
				double evaluation = CoordinatorUtils.evaluateModification(p, evaList, avalaibleRessource);
				evaluation = CoordinatorUtils.round(evaluation, 3);
				if(evaluation<0) {
					
					if((mR.getAgentAttr()!="")&&!mR.getAgentAttr().isEmpty()) {
						//filter the modification list by the agent attribute
						newEvaList = DefaultCoordinatorFuctions.filterUsingAgentAttribute(evaList, mR.getAgentAttr(), a.getScope(), avalaibleRessource);
						//System.out.println("Protocol : Attribute Filter : " + mR.getAgentAttr());
					}
					
					if(mR.isExtra_comp()) {
						//filter the modification list by using an extraspecific competition function
						newEvaList = DefaultCoordinatorFuctions.extraspecificCompetition(evaList, mR.getDom_spec(),avalaibleRessource);
						//System.out.println("Protocol : Interspecific Competition");
					}
					
					if(mR.isIntra_comp()) {
						//filter the modification list by using an intra specific competition function
						newEvaList = DefaultCoordinatorFuctions.intraspecificCompetition(evaList,a.getScope(),avalaibleRessource);
						//System.out.println("Protocol : intraspecific Competition");
					}
					
					if(mR.isFair_dist()) {
						//change the value of the modification so the ressource consumption is fair among agents
						newEvaList = DefaultCoordinatorFuctions.fairModification(evaList, avalaibleRessource);
						//System.out.println("Protocol : Even Distribution");
					}
					
					if(!newEvaList.isEmpty()) {
						for (Modification mod : newEvaList) {
							avalaibleRessource = avalaibleRessource + mod.value;
						}
					
						a.setAttribute(p, avalaibleRessource);
					}else {
						a.setAttribute(p, CoordinatorUtils.noRessource(p, evaList, avalaibleRessource));						
					}
					
				}
				else {		
					evaList.forEach(m -> m.addModificationToAgent(m.agent));					
					a.setAttribute(p, evaluation);
				}
			}else if(avalaibleRessource==0) {
				a.setAttribute(p, CoordinatorUtils.noRessource(p, evaList, avalaibleRessource));
			}
			
			//write file here
			
			
			//delete the curent evaluation list from the sorted modificaiton list
			for (Modification modification : evaList) {
				modSortedByParameters.remove(modification);
			}
			
		}
		a.setAttribute(IConflictResolverSkill.MODIFICATION_LIST, new LinkedList<>());
	}
	
}
