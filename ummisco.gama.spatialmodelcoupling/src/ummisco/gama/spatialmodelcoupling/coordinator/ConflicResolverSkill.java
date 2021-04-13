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
		type = IType.LIST, init = "[]")
	 }
	 )
public class ConflicResolverSkill extends Skill{
	
	@action(name = IConflictResolverSkill.APPLY_MODIFICATION)
	public void applyModifications(IScope scope) {
		IAgent a = scope.getAgent();
		List<Modification> mods = (List<Modification>)a.getAttribute(IConflictResolverSkill.MODIFICATION_LIST);
		
		//sort Modification by time
		mods = CoordinatorUtils.sortModificationsByTime(mods); 
		
		//sort by time and parameter each element represents a modification on the same parameter at the same time
		LinkedList<LinkedList<Modification>> modSorted = CoordinatorUtils.groupByTime(mods); 
		
		for (LinkedList<Modification> evaList : modSorted) {
			//evaluate the parameter
			Modification firstModification = evaList.iterator().next();
			String parameter = firstModification.getParameter();
			double avalaibleRessource = (Double) a.getAttribute(parameter);			
			
			if(!(avalaibleRessource==0)) {
				double evaluation = CoordinatorUtils.evaluateModification(parameter, evaList, avalaibleRessource);
				if(evaluation<0) {
					//handle conflict
					List<ModelRelation> mRL = (List<ModelRelation>) a.getAttribute(IConflictResolverSkill.MODEL_INTERACTION); 
					ModelRelation mR = CoordinatorUtils.getModelInteraction(parameter, mRL);
					
					a.getSpecies().getAction(mR.getCoordinationMethod()).executeOn(scope);
					
				}
				else {
					a.setAttribute(parameter, evaluation);
				}
			}
		}
		
		a.setAttribute(IConflictResolverSkill.MODIFICATION_LIST, new LinkedList<>());
		
	}
	
	@action(name = IConflictResolverSkill.COORDINATOR_NAME,
			args = {
					@arg(name = IConflictResolverSkill.INVOLVED_PARAMETER, type = IType.STRING),
					@arg(name = IConflictResolverSkill.FUNCTION_NAME, type = IType.STRING),
					@arg(name = IConflictResolverSkill.FILTER_BY_ATTRIBUTE, type = IType.STRING, optional=true),
					@arg(name = IConflictResolverSkill.INTRA_COMPETITION, type = IType.BOOL, optional = true),
					@arg(name = IConflictResolverSkill.EXTRA_COMPETITION, type = IType.BOOL, optional = true),
					@arg(name = IConflictResolverSkill.FAIR_DISTRIBUTION,type = IType.BOOL, optional = true)
			}
			)
	public void setCoordinatorFunction(IScope scope) {
		IAgent a = scope.getAgent();
		List<ModelRelation> list = (List<ModelRelation>)a.getAttribute(IConflictResolverSkill.MODEL_INTERACTION);
		
		String param = scope.getArg(IConflictResolverSkill.INVOLVED_PARAMETER, IType.STRING).toString();
		String fct = scope.getArg(IConflictResolverSkill.FUNCTION_NAME, IType.STRING).toString();
		
		String attr = scope.getArg(IConflictResolverSkill.FILTER_BY_ATTRIBUTE, IType.STRING).toString();
		boolean intra_comp = scope.getBoolArg(IConflictResolverSkill.INTRA_COMPETITION);
		boolean extra_comp = scope.getBoolArg(IConflictResolverSkill.EXTRA_COMPETITION);
		boolean fair_dist = scope.getBoolArg(IConflictResolverSkill.FAIR_DISTRIBUTION);
		
		ModelRelation mR = new ModelRelation(param, fct);
		list.add(mR);
		
		a.setAttribute(IConflictResolverSkill.MODEL_INTERACTION, list);
	}
	
	

}
