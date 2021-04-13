package ummisco.gama.spatialmodelcoupling.coordinator;

import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.IScope;
import msi.gaml.skills.Skill;
import msi.gaml.types.IType;
import ummisco.gama.spatialmodelcoupling.coordinator.IConflictResolverSkill;


@skill(name = IConflictResolverSkill.SKILL_NAME, concept= {IConcept.SKILL})
@vars({
		@variable(name = IConflictResolverSkill.MODIFICATION_LIST,
		type = IType.LIST, init ="[]" ),
		
		@variable(name = "evaluation_list",
		type = IType.LIST, init = "[]")
	 }
	 )
public class ConflicResolverSkill extends Skill{
	
	@action(name = IConflictResolverSkill.APPLY_MODIFICATION)
	public void applyModifications(IScope scope) {
		
	}
	
	

}
