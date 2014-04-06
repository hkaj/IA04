package sudoku_solver;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AgentRegisterBehaviour extends OneShotBehaviour {

	public AgentRegisterBehaviour(SimulAgent a, ACLMessage order) {
		super(a);
		m_order = order;
		this.m_myAgent = a;
	}
	
	@Override
	public void action() {
		String content = m_order.getContent();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			AID agentAID = new AID(jrootNode.path("content").path("AID").path(0).textValue(), true);
			m_myAgent.add_m_anaAgents(agentAID);
			informEnv(agentAID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
