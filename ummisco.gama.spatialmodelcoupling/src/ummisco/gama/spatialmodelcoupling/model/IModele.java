package ummisco.gama.spatialmodelcoupling.model;

public interface IModele {
	
	public final static String SKILL_NAME = "coupled_model";
	public final static String MODEL_STEP = "step";
	public final static String EXECUTION = "is_allowed_to_run";
	public final static String NEXT_EXECUTION_DATE = "next_execution_date";
	public final static String MODEL_IN_CONFLICT = "model_in_conflict";
	public final static String PRIORITY = "priority";
	public final static String SPACE_PARTICLE = "space_particle";
	
	public final static String SET_SPACE_PARTICLE = "set_space_particle";
	public final static String SPACE = "space";
	
	public final static String REQUEST_MODIFICATION = "request_modification";
	public final static String PARAMETER = "parameter";
	public final static String VALUE = "value";
	
	public final static String EXPLOITED_RESSOURCES = "exploited_ressource";
	public final static String GET_RESSOURCE = "get_ressource";
	public final static String RESSOURCE = "name";

}
