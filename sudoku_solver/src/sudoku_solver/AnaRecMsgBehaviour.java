package sudoku_solver;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnaRecMsgBehaviour extends CyclicBehaviour {

	public AnaRecMsgBehaviour(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
		ACLMessage order = myAgent.receive();
		if (order != null) {
			if (order.getPerformative() == ACLMessage.REQUEST) {
				// process the request
				String content = order.getContent();
				ObjectMapper mapper = new ObjectMapper();
				ArrayList<Case> array = new ArrayList<Case>();
				try {
					JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
					array = mapper.readValue(
							jrootNode.path("content").path("cases").textValue(),
							new TypeReference<List<Case>>(){}
					);
					m_myAgent.set_m_array(array);
				} catch (Exception e) {
					e.printStackTrace();
				}
				m_myAgent.addBehaviour(new ProcessRequestBehaviour(m_myAgent));
			} else {
				System.out.println("AnaAgent received an unexpected message.");
				System.out.println("Agent " + order.getSender() + "sent a " + order.getPerformative() + ": " + order.getContent());
			}
		}
	}
	
	// Members
	private AnaAgent m_myAgent;

}
