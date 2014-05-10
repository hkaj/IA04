package ontologie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SparqlShowKBAnswer extends OneShotBehaviour {

	private ACLMessage m_message;
	
	public SparqlShowKBAnswer(Agent myAgent, ACLMessage message) {
		m_message = message;
	}
	@Override
	public void action() {
		ObjectMapper mapper = new ObjectMapper();
		List<List<String>> result = new ArrayList<List<String>>();
		final JsonNode contentNode;
		try {
			contentNode = mapper.readValue(m_message.getContent(), JsonNode.class).path("content");
			for (Iterator<JsonNode> assertions = contentNode.path("assert").elements(); assertions.hasNext();) {
				List<String> tmp = Arrays.asList(assertions.next().asText().replace("[", "").replace("]", "").split(", "));
				result.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("The KB answered with " + result.size() + " element(s).");
		System.out.println("The results are: ");
		for(List<String> r : result) {
			System.out.println(r);
		}

	}

}
