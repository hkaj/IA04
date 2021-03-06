package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResultHandleBehaviour extends OneShotBehaviour {

	public ResultHandleBehaviour(SimulAgent a, ACLMessage order) {
		super(a);
		m_myAgent = a;
		m_order = order;
	}

	@Override
	public void action() {
		String content = m_order.getContent();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Case> resultPart = new ArrayList<Case>();
		try {
			// Map the result from the message to a an array.
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			String json = jrootNode.path("content").path("cases").toString();
			resultPart = mapper.readValue(json, new TypeReference<ArrayList<Case>>(){});					
			// Append the result part to the result of the iteration, and reference it with the AID of the worker.
			m_myAgent.put_m_result(m_order.getSender(), resultPart);
			// Once that every AnaAgent answered we send the result of the iteration to the EnvAgent.
			if (m_myAgent.get_m_result().size() == 27) {
				m_myAgent.addBehaviour(new sendResultsBehaviour(m_myAgent));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Members
	private SimulAgent m_myAgent;
	private ACLMessage m_order;
}
