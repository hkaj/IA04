package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

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
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			String json = jrootNode.path("content").path("cases").toString();
			System.out.println("Content: " + content);
			System.out.println("JSON: " + json);
			resultPart = mapper.readValue(json, new TypeReference<ArrayList<Case>>(){});					
			m_myAgent.put_m_result(m_order.getSender(), resultPart);
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
