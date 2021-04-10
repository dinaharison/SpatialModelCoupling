package ummisco.gama.spatialmodelcoupling.types;

import java.util.Iterator;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;
import msi.gaml.species.ISpecies;
import ummisco.gama.spatialmodelcoupling.model.IModele;

public class ProcessDate {
		private ISpecies process;
		private double step; //second
		private double nextExecutionDate;
		private int executionCount = 0;

		public ProcessDate(ISpecies process, double step) {
			super();
			this.process = process;
			this.step = step;
			this.nextExecutionDate = step;
		}
		
		public void setExecutionCount(int i) {
			this.executionCount = i;
		}
		
		public int getExecutionCount() {
			return this.executionCount;
		}
				
		public ISpecies getProcess() {
			return process;
		}
		public void setProcess(ISpecies process) {
			this.process = process;
		}
		public double getStep() {
			return step;
		}
		public void setStep(double step) {
			this.step = step;
		}
		public double getNextExecutionDate() {
			return nextExecutionDate;
		}
		
		public void proceedToNextDate() {
			this.nextExecutionDate = this.nextExecutionDate + this.step;
			//System.out.println("ProcessDateClass : " + process.getName() +"next exe : "+this.nextExecutionDate);
		}
		
		public void updateAgentStep(IScope scope) {
			Iterator<IAgent> IagentList =
			(Iterator<IAgent>) this.process.getPopulation(scope).getAgents(scope).iterable(scope).iterator();
			
			while(IagentList.hasNext()) {
				IAgent a = IagentList.next();			
				a.setAttribute(IModele.NEXT_EXECUTION_DATE, this.nextExecutionDate);
			}
		}
		
		public String toString() {
			return "process : " + this.process.getName() +"\n"+ 
					"Step : " + this.getStep() + "\n" + 
					"Next Execution date : " + this.getNextExecutionDate() + "\n" + "\n";
		}
		
}
