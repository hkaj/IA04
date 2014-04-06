package sudoku_solver;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
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
					String json = jrootNode.path("content").path("cases").toString();
					array = mapper.readValue(json, new TypeReference<ArrayList<Case>>(){});
					m_myAgent.set_m_array(array);
//					System.out.println("LIGNE RECUE");
//					for (Case c : array) {
//						System.out.println("VALUE : "+c.getValue());
//						System.out.println("POS : "+c.getPossibilities());
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				ProcessRequestBehaviour processBehaviour = new ProcessRequestBehaviour(m_myAgent);
				processBehaviour.addSubBehaviour(new RemoveFoundValues(m_myAgent));
				processBehaviour.addSubBehaviour(new DetermineSingleValue(m_myAgent));
				processBehaviour.addSubBehaviour(new NarrowDown2Values(m_myAgent));
				processBehaviour.addSubBehaviour(new ReturnProcessResult(m_myAgent));
				m_myAgent.addBehaviour(processBehaviour);
			} else {
				System.out.println("AnaAgent received an unexpected message.");
				System.out.println("Agent " + order.getSender() + "sent a " + order.getPerformative() + ": " + order.getContent());
			}
		}
	}
	
	// Members
	private AnaAgent m_myAgent;

}
