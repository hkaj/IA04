package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class sendResultsBehaviour extends OneShotBehaviour {

	public sendResultsBehaviour(SimulAgent a) {
		super(a);
		m_myAgent = a;
	}
	
	public void action() {
		System.out.println("All the AnaAgents returned. Sending the result to EnvAgent.");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(m_myAgent.m_envAgent);
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			message.setContent(mapper.writeValueAsString(m_myAgent.m_result));
			myAgent.send(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
		m_myAgent.m_result.clear();
	}
	
	// Members
	private SimulAgent m_myAgent; 

}
