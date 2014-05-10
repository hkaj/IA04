package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SparqlShowKBAnswer extends OneShotBehaviour {

	private ACLMessage m_message;
	
	public SparqlShowKBAnswer(Agent myAgent, ACLMessage message) {
		m_message = message;
	}
	@Override
	public void action() {
		ObjectMapper mapper = new ObjectMapper();
		List<String> vars = new ArrayList<String>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		final JsonNode varNode;
		final JsonNode resultNode;
//		System.out.println("Response: " + m_message.getContent());
		try {
			varNode = mapper.readValue(m_message.getContent(), JsonNode.class).path("head");
			resultNode = mapper.readValue(m_message.getContent(), JsonNode.class).path("results");
			for (Iterator<JsonNode> fields= varNode.path("vars").elements(); fields.hasNext();) {
				vars.add(fields.next().asText());
			}
			for (Iterator<JsonNode> bindings = resultNode.path("bindings").elements(); bindings.hasNext();) {
				HashMap<String, String> res = new HashMap<String, String>();
				JsonNode binding = bindings.next();
				for (String v : vars) {
					res.put(v, binding.path(v).path("value").asText());
				}
				result.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("The KB answered with " + result.size() + " element(s).");
		System.out.println("The results are: ");
		for(HashMap<String, String> r : result) {
			System.out.println(r);
		}
	}
}
