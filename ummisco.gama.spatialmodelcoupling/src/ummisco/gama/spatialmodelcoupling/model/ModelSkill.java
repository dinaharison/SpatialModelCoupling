package ummisco.gama.spatialmodelcoupling.model;

import msi.gama.precompiler.IConcept;
import msi.gama.runtime.IScope;

import java.util.List;
import java.util.Map;

import msi.gama.kernel.model.IModel;
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
	@variable(name = IModele.SPACE_PARTICLE, type = IType.AGENT),
	@variable(name = IModele.EXPLOITED_RESSOURCES, type = IType.LIST, init="[]")
	
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
		return (IAgent)scope.getAgent().getAttribute(IModele.SPACE_PARTICLE) ;
	}
	
	@action(name = IModele.REQUEST_MODIFICATION, args = {
			
			@arg(name = IModele.PARAMETER, 
				 type = IType.STRING,
				 optional = false),
			
			@arg(name = IModele.VALUE, 
				 type = IType.FLOAT,
				 optional = false) 
	})
	public void requestModification(IScope scope) {
		
		//add a try catch
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
		}
	}
	
	@action(name = IModele.SET_SPACE_PARTICLE, args = {
			@arg(name = IModele.SPACE,
				 type = IType.AGENT,
				 optional = false)
	})
	public void setSpaceParticle(IScope scope) {
		IAgent a = scope.getAgent();
		IAgent p = (IAgent) scope.getArg(IModele.SPACE, IType.AGENT);		
		a.setLocation(p.getLocation());
		
		a.setAttribute(IModele.SPACE_PARTICLE, p);
	}
	
	@action(name = IModele.GET_RESSOURCE, args = {
			@arg(name = IModele.RESSOURCE,
					type = IType.STRING,
					optional = false)
	})
	public Double getRessource(IScope scope) {
		IAgent a = scope.getAgent();
		String ressource = scope.getArg(IModele.RESSOURCE, IType.STRING).toString();
		List<Modification> modifications = (List<Modification>) a.getAttribute(IModele.EXPLOITED_RESSOURCES);
		Modification mod = modifications.stream().filter(modif -> modif.getParameter().equals(ressource)).findAny().orElse(null);
		if(mod!=null) {return mod.value;}
		else {return null;}
	}
}
