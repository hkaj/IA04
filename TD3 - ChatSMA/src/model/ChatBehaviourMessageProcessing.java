package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ChatBehaviourMessageProcessing extends OneShotBehaviour {

	public ChatBehaviourMessageProcessing(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
	}

	@Override
	public void action() {
		String content = m_message.getContent();
		String message = null;
		ObjectMapper mapper = new ObjectMapper();
		
		
		try {
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			message =  jrootNode.path("content").path("message").asText();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (message != null){
			ChatAgent agent = (ChatAgent) myAgent;
			agent.addNewMessage(message, m_message.getSender());
		}

	}
	
	ACLMessage m_message;

}
