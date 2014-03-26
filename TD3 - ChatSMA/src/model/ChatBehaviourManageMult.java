package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ChatBehaviourManageMult extends OneShotBehaviour {

	public ChatBehaviourManageMult(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
	}

	@Override
	public void action() {
		int nb1 = 0, nb2 = 0;
		int res = 1;

		//Parsing received JSON message from MultAgent
		String messageContent = m_message.getContent();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jrootNode = mapper.readValue(messageContent, JsonNode.class);
			nb1 = jrootNode.path("content").path("numbers").path(0).intValue();
			nb2 = jrootNode.path("content").path("numbers").path(1).intValue();
			res = jrootNode.path("content").path("result").path(0).intValue();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		String text = "Le résultat de la multiplication " + Integer.toString(nb1) + " x " + Integer.toString(nb2) + " est " + Integer.toString(res) + ".";
		
		ChatAgent agent = (ChatAgent) myAgent;
		agent.addNewMessage(text, agent.getAID());
	}

	
	private ACLMessage m_message;
}
