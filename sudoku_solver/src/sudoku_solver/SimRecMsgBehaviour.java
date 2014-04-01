package sudoku_solver;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class SimRecMsgBehaviour extends CyclicBehaviour {
	
	public SimRecMsgBehaviour(SimulAgent agent) {
		this.myAgent = (Agent) agent;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}
	
	// Members
	ArrayList<AnaAgent> m_anaAgents; 

}
