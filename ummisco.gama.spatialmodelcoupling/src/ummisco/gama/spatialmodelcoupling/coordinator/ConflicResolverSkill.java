package ummisco.gama.spatialmodelcoupling.coordinator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.IScope;
import msi.gaml.operators.Cast;
import msi.gaml.skills.Skill;
import msi.gaml.species.ISpecies;
import msi.gaml.types.IType;
import ummisco.gama.spatialmodelcoupling.coordinator.IConflictResolverSkill;
import ummisco.gama.spatialmodelcoupling.types.ModelRelation;
import ummisco.gama.spatialmodelcoupling.types.Modification;


@skill(name = IConflictResolverSkill.SKILL_NAME, concept= {IConcept.SKILL})
@vars({
		@variable(name = IConflictResolverSkill.MODIFICATION_LIST,
		type = IType.LIST, init ="[]" ),
		@variable(name = IConflictResolverSkill.MODEL_INTERACTION,
		type = IType.LIST, init = "[]"),
		@variable(name = IConflictResolverSkill.IS_CONFLICTED,
		type = IType.BOOL, init = "false")
	 }
	 )
public class ConflicResolverSkill extends Skill{
	
	@action(name = IConflictResolverSkill.APPLY_MODIFICATION)
	public static void applyModifications(IScope scope) {
		IAgent a = scope.getAgent();
		List<Modification> mods = (List<Modification>)a.getAttribute(IConflictResolverSkill.MODIFICATION_LIST);
		
		//sort Modification by time
		mods = CoordinatorUtils.sortModificationsByTime(mods);	
		
		//sort by time and parameter each element represents a modification on the same parameter at the same time
		LinkedList<LinkedList<Modification>> modSorted = CoordinatorUtils.groupByTime(mods);
		
		for (LinkedList<Modification> evaList : modSorted) {
			//evaluate the parameter
			if(!evaList.isEmpty()) {
				Modification firstModification = evaList.getFirst();
				String p = firstModification.getParameter();
				
				List<ModelRelation> mRL = (List<ModelRelation>) a.getAttribute(IConflictResolverSkill.MODEL_INTERACTION); 
				ModelRelation mR = CoordinatorUtils.getModelInteraction(p, mRL);
				LinkedList<Modification> newEvaList = evaList;
				
				double avalaibleRessource = Cast.asFloat(scope, a.getAttribute(p));				
				
				if(avalaibleRessource>0) {				
					double evaluation = CoordinatorUtils.evaluateModification(p, evaList, avalaibleRessource);
					if(evaluation<0) {
						//handle conflict
						
						System.out.println("*********DEFAULT LIST*******************");
						evaList.stream().forEach(m -> System.out.println(m.toString()));
						
						if((mR.getAgentAttr()!="")&&!mR.getAgentAttr().isEmpty()) {
							//filter the modification list by the agent attribute
							newEvaList = DefaultCoordinatorFuctions.filterUsingAgentAttribute(evaList, mR.getAgentAttr(), scope, avalaibleRessource);
						}
						
						if(mR.isExtra_comp()) {
							//filter the modification list by using an extraspecific competition function
							newEvaList = DefaultCoordinatorFuctions.extraspecificCompetition(evaList, mR.getDom_spec(),avalaibleRessource);
						}
						
						if(mR.isIntra_comp()) {
							//filter the modification list by using an intra specific competition function
							newEvaList = DefaultCoordinatorFuctions.intraspecificCompetition(evaList,scope,avalaibleRessource);
						}
						
						if(mR.isFair_dist()) {
							//change the value of the modification so the ressource consumption is fair among agents
							newEvaList = DefaultCoordinatorFuctions.fairModification(evaList, avalaibleRessource);
						}
						
						for (Modification mod : newEvaList) {
							double s = evaluation + mod.value;
							if(s>=0) {
								evaluation = s;
							}
						}
						
						System.out.println("**************SORTED LIST*****************************");
						
						newEvaList.stream().forEach(e->System.out.println(e.toString()));
						a.setAttribute(p, newEvaList);
						
					}
					else {
						a.setAttribute(p, evaluation);
					}
				}else if(avalaibleRessource==0) {
					a.setAttribute(p, CoordinatorUtils.noRessource(p, evaList, avalaibleRessource));
				}
			}
		}
		
		a.setAttribute(IConflictResolverSkill.MODIFICATION_LIST, new LinkedList<>());
		
	}
	
	@action(name = IConflictResolverSkill.COORDINATOR_NAME,
			args = {
					@arg(name = IConflictResolverSkill.INVOLVED_PARAMETER, type = IType.STRING),
					@arg(name = IConflictResolverSkill.FILTER_BY_ATTRIBUTE, type = IType.STRING, optional=true),
					@arg(name = IConflictResolverSkill.INTRA_COMPETITION, type = IType.BOOL, optional = true),
					@arg(name = IConflictResolverSkill.DOMINANT_SPEC, type = IType.SPECIES, optional = true),
					@arg(name = IConflictResolverSkill.INTER_COMPETITION, type = IType.BOOL, optional = true),
					@arg(name = IConflictResolverSkill.FAIR_DISTRIBUTION,type = IType.BOOL, optional = true)
			}
	)
	public void setCoordinatorFunction(IScope scope) {
		IAgent a = scope.getAgent();
		List<ModelRelation> list = (List<ModelRelation>)a.getAttribute(IConflictResolverSkill.MODEL_INTERACTION);
		
		String param = scope.getArg(IConflictResolverSkill.INVOLVED_PARAMETER, IType.STRING).toString();
		
		String attr = "";
		if(scope.getArg(IConflictResolverSkill.FILTER_BY_ATTRIBUTE, IType.STRING)!=null) {
			attr = scope.getArg(IConflictResolverSkill.FILTER_BY_ATTRIBUTE, IType.STRING).toString();
		}	
		
		boolean intra_comp = scope.getBoolArg(IConflictResolverSkill.INTRA_COMPETITION);
		boolean extra_comp = scope.getBoolArg(IConflictResolverSkill.INTER_COMPETITION);
		boolean fair_dist = scope.getBoolArg(IConflictResolverSkill.FAIR_DISTRIBUTION);
		ISpecies dom_spec = (ISpecies) scope.getArg(IConflictResolverSkill.DOMINANT_SPEC, IType.SPECIES);
		
		ModelRelation mR = new ModelRelation(param, attr, intra_comp, extra_comp, fair_dist, dom_spec);
		list.add(mR);
		
		a.setAttribute(IConflictResolverSkill.MODEL_INTERACTION, list);
	}
	
	
	

}
