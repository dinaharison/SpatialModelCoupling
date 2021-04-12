package ummisco.gama.spatialmodelcoupling.types;

import com.google.common.base.MoreObjects.ToStringHelper;

import msi.gama.metamodel.agent.IAgent;
import ummisco.gama.spatialmodelcoupling.model.IModele;

public class Modification {
		public IAgent agent;
		public double value;
		public double executedTime;
		//public String operation;

		public Modification(IAgent a, double v) {
			this.agent = a;
			this.value = v;
			this.executedTime = (Double) a.getAttribute(IModele.NEXT_EXECUTION_DATE);
			//this.operation = o;
		}
		
		public String ToString() {
			return "Agent : " + this.agent.getName() + " Modification Value : " + this.value;
		}
		
		public Double getExecutedTime() {
			return this.executedTime;
		}
}

//créer sur gaml
