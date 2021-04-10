package ummisco.gama.spatialmodelcoupling.coordinator;

public interface IConflictResolverSkill {
	public final static String SKILL_NAME = "conflict_resolver";
	
	public final static String RESOLVE = "resolve";
	public final static String ALTER_BEHAVIOUR = "modify_behaviour";
	
	public final static String CONFLICT_TYPE = "conflict_type";
	public final static String CONFLICT_DEFAULT_COMPETITION = "competition";
	public final static String CONFLICT_DEFAULT_COOPERATION = "cooperation";
	
	public final static String PARAMETER_LIST = "parameter_list";
	public final static String VALUE_CHANGER_LIST = "value_changer";
	
	public final static String MODIFICATION_LIST = "modification_list";
}
