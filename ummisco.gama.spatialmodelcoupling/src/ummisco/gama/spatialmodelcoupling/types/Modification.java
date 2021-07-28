package ummisco.gama.spatialmodelcoupling.types;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.common.base.MoreObjects.ToStringHelper;

import msi.gama.metamodel.agent.IAgent;
import ummisco.gama.spatialmodelcoupling.model.IModele;

public class Modification {
		public IAgent agent;
		public double value;
		public double executedTime;
		private String param;
		//public String operation;

		public Modification(IAgent a, double v, String param) {
			this.agent = a;
			this.value = v;
			this.executedTime = (Double) a.getAttribute(IModele.NEXT_EXECUTION_DATE);
			this.param = param;
			//this.operation = o;
		}
		
		public String toString() {
			return "Agent : " + this.agent.getName() +  " Parameter : " + this.param +" Modification Value : " + this.value + " Exe at : " + this.executedTime;
		}
		
		public Double getExecutedTime() {
			return this.executedTime;
		}
		
		public String getParameter() {
			return this.param;
		}
		
		public void changeAgentModifications(){
			this.agent.setAttribute(IModele.EXPLOITED_RESSOURCES, this);
		}
		
		public void addModificationToAgent(IAgent a) {
			ArrayList<Modification> agentCurrentList = (ArrayList<Modification>) a.getAttribute(IModele.EXPLOITED_RESSOURCES);
			agentCurrentList.add(this);
			a.setAttribute(IModele.EXPLOITED_RESSOURCES, agentCurrentList);
		}
		
		public void addModificationsToAgent(IAgent a, LinkedList<Modification> mods) {
			ArrayList<Modification> agentCurrentList = (ArrayList<Modification>) a.getAttribute(IModele.EXPLOITED_RESSOURCES);
			agentCurrentList.addAll(mods);
			a.setAttribute(IModele.EXPLOITED_RESSOURCES, agentCurrentList);
		}
}

//créer sur gaml
