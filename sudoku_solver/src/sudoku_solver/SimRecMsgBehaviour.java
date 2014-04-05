package sudoku_solver;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SimRecMsgBehaviour extends CyclicBehaviour {

	public SimRecMsgBehaviour(SimulAgent agent) {
		super(agent);
		this.m_myAgent = agent;
	}

	@Override
	public void action() {
		ACLMessage order = myAgent.receive();
		if (order != null) {
			if (order.getPerformative() == ACLMessage.SUBSCRIBE) {
				// register the analysis agents
				m_myAgent.addBehaviour(new AgentRegisterBehaviour(m_myAgent, order));
			} else if (order.getPerformative() == ACLMessage.INFORM) {
				// handle the analysis-agent responses
				m_myAgent.addBehaviour(new ResultHandleBehaviour(m_myAgent, order));
			} else if (order.getPerformative() == ACLMessage.CONFIRM) {
				System.out.println("The sudoku is solved! This simulation is now over.");
			} else {
				System.out.println("SimulAgent received an unexpected message.");
			}
		}
	}

	// Members
	SimulAgent m_myAgent;


}
