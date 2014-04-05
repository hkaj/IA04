package sudoku_solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SimRecMsgBehaviour extends CyclicBehaviour {
	
	public SimRecMsgBehaviour(SimulAgent agent) {
		this.m_myAgent = agent;
		this.m_envAgent = new AID("EnvAgent", true);
		
	}

	@Override
	public void action() {
		ACLMessage order = myAgent.receive();
		if (order != null) {
			if (order.getPerformative() == ACLMessage.SUBSCRIBE) {
				// register the analysis agents
				String content = order.getContent();
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
					AID agentAID = new AID(jrootNode.path("content").path("AID").path(0).textValue(), true);
					m_anaAgents.add(agentAID);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (order.getPerformative() == ACLMessage.INFORM) {
				// handle the analysis agents responses
				String content = order.getContent();
				ObjectMapper mapper = new ObjectMapper();
				List<Case> resultPart = new ArrayList<Case>(9);
				int idx = 0;
				try {
					JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
					idx = jrootNode.path("content").path("index").path(0).intValue();
					resultPart = mapper.readValue(jrootNode.path("content").path("cases").textValue(), new TypeReference<List<Case>>(){});					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			} else {
				System.out.println("SimulAgent received an unexpected message");
			}
		}

	}
	
	// Members
	ArrayList<AID> m_anaAgents;
	AID m_envAgent;
	SimulAgent m_myAgent;
	ArrayList<ArrayList<Case>> m_result;

}
