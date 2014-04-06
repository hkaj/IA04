package sudoku_solver;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentRegisterBehaviour extends OneShotBehaviour {

	public AgentRegisterBehaviour(SimulAgent a, ACLMessage order) {
		super(a);
		m_order = order;
		this.m_myAgent = a;
	}
	
	@Override
	public void action() {
		AID agentAID = m_order.getSender();
		m_myAgent.add_m_anaAgents(agentAID);
		informEnv(agentAID);
	}
	
	// Members
	private ACLMessage m_order;
	private SimulAgent m_myAgent;
	
	private void informEnv(AID aid) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM_REF);
		message.addReplyTo(aid);
		message.addReceiver(m_myAgent.get_m_envAgent());
		myAgent.send(message);
	}

}
