package gaml.additions;
import msi.gaml.extensions.multi_criteria.*;
import msi.gama.outputs.layers.charts.*;
import msi.gama.outputs.layers.*;
import msi.gama.outputs.*;
import msi.gama.kernel.batch.*;
import msi.gama.kernel.root.*;
import msi.gaml.architecture.weighted_tasks.*;
import msi.gaml.architecture.user.*;
import msi.gaml.architecture.reflex.*;
import msi.gaml.architecture.finite_state_machine.*;
import msi.gaml.species.*;
import msi.gama.metamodel.shape.*;
import msi.gaml.expressions.*;
import msi.gama.metamodel.topology.*;
import msi.gaml.statements.test.*;
import msi.gama.metamodel.population.*;
import msi.gama.kernel.simulation.*;
import msi.gama.kernel.model.*;
import java.util.*;
import msi.gaml.statements.draw.*;
import  msi.gama.metamodel.shape.*;
import msi.gama.common.interfaces.*;
import msi.gama.runtime.*;
import java.lang.*;
import msi.gama.metamodel.agent.*;
import msi.gaml.types.*;
import msi.gaml.compilation.*;
import msi.gaml.factories.*;
import msi.gaml.descriptions.*;
import msi.gama.util.tree.*;
import msi.gama.util.file.*;
import msi.gama.util.matrix.*;
import msi.gama.util.graph.*;
import msi.gama.util.path.*;
import msi.gama.util.*;
import msi.gama.runtime.exceptions.*;
import msi.gaml.factories.*;
import msi.gaml.statements.*;
import msi.gaml.skills.*;
import msi.gaml.variables.*;
import msi.gama.kernel.experiment.*;
import msi.gaml.operators.*;
import msi.gama.common.interfaces.*;
import msi.gama.extensions.messaging.*;
import msi.gama.metamodel.population.*;
import msi.gaml.operators.Random;
import msi.gaml.operators.Maths;
import msi.gaml.operators.Points;
import msi.gaml.operators.Spatial.Properties;
import msi.gaml.operators.System;
import static msi.gaml.operators.Cast.*;
import static msi.gaml.operators.Spatial.*;
import static msi.gama.common.interfaces.IKeyword.*;
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })

public class GamlAdditions extends AbstractGamlAdditions {
	public void initialize() throws SecurityException, NoSuchMethodException {
	initializeVars();
	initializeAction();
	initializeSkill();
}public void initializeVars()  {
_var(ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class,desc(5,S("type","5","name","modification_list","init","[]")),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class,desc(5,S("type","5","name","model_interaction","init","[]")),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class,desc(3,S("type","3","name","is_conflicted","init",FALSE)),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2.class,desc(5,S("type","5","name","modification_list","init","[]")),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2.class,desc(5,S("type","5","name","model_interaction","init","[]")),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,desc(2,S("type","2","name","step")),(s,a,t,v)->t==null? 0d:((ummisco.gama.spatialmodelcoupling.model.ModelSkill)t).getModelStep(s),null,null);
_var(ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,desc(2,S("type","2","name","next_execution_date")),(s,a,t,v)->t==null? 0d:((ummisco.gama.spatialmodelcoupling.model.ModelSkill)t).getNextExecutionDate(s),null,null);
_var(ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,desc(3,S("type","3","name","is_allowed_to_run","init",FALSE)),(s,a,t,v)->t==null? false:((ummisco.gama.spatialmodelcoupling.model.ModelSkill)t).getIsExecuted(s),null,null);
_var(ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,desc(11,S("type","11","name","space_particle")),(s,a,t,v)->t==null? null:((ummisco.gama.spatialmodelcoupling.model.ModelSkill)t).getSpaceParticle(s),null,null);
_var(ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,desc(5,S("type","5","name","exploited_ressource","init","[]")),null,null,null);
_var(ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2.class,desc(5,S("type","5","name","calendar")),null,null,null);
}public void initializeAction() throws SecurityException, NoSuchMethodException {
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill) t).setCoordinatorFunction(s);return null;},desc(PRIM,new Children(desc(ARG,NAME,"parameter",TYPE,"4","optional",TRUE),desc(ARG,NAME,"filter_using_model_attribute",TYPE,"4","optional",TRUE),desc(ARG,NAME,"intraspecific_competition",TYPE,"3","optional",TRUE),desc(ARG,NAME,"dominant_species",TYPE,"14","optional",TRUE),desc(ARG,NAME,"interspecific_competition",TYPE,"3","optional",TRUE),desc(ARG,NAME,"fair_distribution",TYPE,"3","optional",TRUE)),NAME,"define_coordinator_function",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class.getMethod("setCoordinatorFunction",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill) t).applyModifications(s);return null;},desc(PRIM,new Children(),NAME,"apply_modification",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class.getMethod("applyModifications",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2) t).setCoordinatorFunction(s);return null;},desc(PRIM,new Children(desc(ARG,NAME,"parameter",TYPE,"4","optional",TRUE),desc(ARG,NAME,"filter_using_model_attribute",TYPE,"4","optional",TRUE),desc(ARG,NAME,"intraspecific_competition",TYPE,"3","optional",TRUE),desc(ARG,NAME,"dominant_species",TYPE,"14","optional",TRUE),desc(ARG,NAME,"interspecific_competition",TYPE,"3","optional",TRUE),desc(ARG,NAME,"fair_distribution",TYPE,"3","optional",TRUE)),NAME,"define_coordinator_function",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2.class.getMethod("setCoordinatorFunction",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2) t).applyModifications(s);return null;},desc(PRIM,new Children(),NAME,"apply_modification",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2.class.getMethod("applyModifications",SC));
_action((s,a,t,v)->((ummisco.gama.spatialmodelcoupling.model.ModelSkill) t).getRessource(s),desc(PRIM,new Children(desc(ARG,NAME,NAME,TYPE,"4","optional",FALSE)),NAME,"get_ressource",TYPE,Ti(D),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.model.ModelSkill.class.getMethod("getRessource",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.model.ModelSkill) t).requestModification(s);return null;},desc(PRIM,new Children(desc(ARG,NAME,"parameter",TYPE,"4","optional",FALSE),desc(ARG,NAME,"value",TYPE,"2","optional",FALSE)),NAME,"request_modification",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.model.ModelSkill.class.getMethod("requestModification",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.model.ModelSkill) t).setSpaceParticle(s);return null;},desc(PRIM,new Children(desc(ARG,NAME,"space",TYPE,"11","optional",FALSE)),NAME,"set_space_particle",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.model.ModelSkill.class.getMethod("setSpaceParticle",SC));
_action((s,a,t,v)->((ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2) t).printCalendar(s),desc(PRIM,new Children(),NAME,"print_calendar",TYPE,Ti(S),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2.class.getMethod("printCalendar",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2) t).executeFirstCalendarEntry(s);return null;},desc(PRIM,new Children(),NAME,"schedule",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2.class.getMethod("executeFirstCalendarEntry",SC));
_action((s,a,t,v)->{((ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2) t).insertProcessList(s);return null;},desc(PRIM,new Children(desc(ARG,NAME,"process",TYPE,"14","optional",FALSE),desc(ARG,NAME,"step",TYPE,"2","optional",FALSE)),NAME,"scheduler_insert",TYPE,Ti(void.class),VIRTUAL,FALSE),ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2.class.getMethod("insertProcessList",SC));
}public void initializeSkill()  {
_skill("conflict_resolver",ummisco.gama.spatialmodelcoupling.coordinator.ConflicResolverSkill.class,AS);
_skill("conflict_resolverV2",ummisco.gama.spatialmodelcoupling.coordinator.ConflictResolverSkillV2.class,AS);
_skill("coupled_model",ummisco.gama.spatialmodelcoupling.model.ModelSkill.class,AS);
_skill("scheduleV2",ummisco.gama.spatialmodelcoupling.scheduler.ScheduleSkillV2.class,AS);
}
}