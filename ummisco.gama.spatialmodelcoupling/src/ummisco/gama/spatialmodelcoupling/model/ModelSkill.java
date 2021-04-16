package ummisco.gama.spatialmodelcoupling.model;

import msi.gama.precompiler.IConcept;
import msi.gama.runtime.IScope;

import java.util.List;
import java.util.Map;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gaml.operators.Cast;
import msi.gaml.skills.Skill;
import msi.gaml.types.IType;
import ummisco.gama.spatialmodelcoupling.coordinator.IConflictResolverSkill;
import ummisco.gama.spatialmodelcoupling.types.Modification;



@vars({
	@variable(name = IModele.MODEL_STEP,type=IType.FLOAT),
	@variable(name = IModele.NEXT_EXECUTION_DATE, type=IType.FLOAT),
	@variable(name = IModele.EXECUTION, type = IType.BOOL, init = "false"),
	@variable(name = IModele.SPACE_PARTICLE, type = IType.AGENT)
	
})
@skill(name = IModele.SKILL_NAME,concept = { IConcept.SKILL })
public class ModelSkill extends Skill {
	
	@getter(IModele.MODEL_STEP)
	public double getModelStep(IScope scope) {
		return (Double) scope.getAgent().getAttribute(IModele.MODEL_STEP);
	}
	
	@getter(IModele.NEXT_EXECUTION_DATE)
	public double getNextExecutionDate(IScope scope) {
		return (Double) scope.getAgent().getAttribute(IModele.NEXT_EXECUTION_DATE);
	}
	
	@getter(IModele.EXECUTION)
	public boolean getIsExecuted(IScope scope) {
		return (boolean) scope.getAgent().getAttribute(IModele.EXECUTION);
	}
	
	@getter(IModele.SPACE_PARTICLE)
	public IAgent getSpaceParticle(IScope scope) {
		return Cast.asAgent(scope, scope.getAgent().getAttribute(IModele.SPACE_PARTICLE)) ;
	}
	
	@action(name = "request_modification", args = {
			
			@arg(name = IModele.PARAMETER, 
				 type = IType.STRING,
				 optional = false),
			
			@arg(name = IModele.VALUE, 
				 type = IType.FLOAT,
				 optional = false) 
	})
	public void requestModification(IScope scope) {
		
		IAgent space = this.getSpaceParticle(scope);
		if(!space.dead()) {
			List<Modification> modl = (List<Modification>) space.getAttribute(IConflictResolverSkill.MODIFICATION_LIST);
			
			IAgent a = scope.getAgent();
			String p = scope.getArg(IModele.PARAMETER, IType.STRING).toString();
			double v = (Double) scope.getArg(IModele.VALUE, IType.FLOAT);
			
			//System.out.println(a.getName() + " executed on : " + a.getAttribute(IModele.NEXT_EXECUTION_DATE) + " cycle : " + scope.getSimulation().getCycle(scope));
			
			Modification mod = new Modification(a, v, p);
			
			modl.add(mod);
			
			space.setAttribute(IConflictResolverSkill.MODIFICATION_LIST, modl);
		}else {
			
			//error
			
		}

		
	}
}
