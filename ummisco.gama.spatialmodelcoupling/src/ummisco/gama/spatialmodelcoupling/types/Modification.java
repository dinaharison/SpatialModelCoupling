package ummisco.gama.spatialmodelcoupling.types;

import com.google.common.base.MoreObjects.ToStringHelper;

import msi.gama.metamodel.agent.IAgent;
import ummisco.gama.spatialmodelcoupling.model.IModele;

public class Modification {
		public IAgent agent;
		public double value;
		public double executedTime;
		public String param;
		//public String operation;

		public Modification(IAgent a, double v, String param) {
			this.agent = a;
			this.value = v;
			this.executedTime = (Double) a.getAttribute(IModele.NEXT_EXECUTION_DATE);
			this.param = param;
			//this.operation = o;
		}
		
		public String ToString() {
			return "Agent : " + this.agent.getName() + " Modification Value : " + this.value;
		}
		
		public Double getExecutedTime() {
			return this.executedTime;
		}
		
		public String getParameter() {
			return this.param;
		}
		
}

//créer sur gaml
