package model;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ChatBehaviourSendMessage extends OneShotBehaviour {

	public ChatBehaviourSendMessage(Agent a, String message) {
		super(a);
		m_message = message;
	}
	
	
	@Override
	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		
		ObjectMapper writerMapper = new ObjectMapper();
		try {
			Map<String, Object> newMessageMap = new HashMap<String, Object>();
			newMessageMap.put("message", m_message);
			Map<String, Object> rootMap = new HashMap<String, Object>();
			rootMap.put("content", newMessageMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, rootMap);
			message.setContent(sw.toString()); 

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//Recherche des receveurs
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType("Chat");
		sd.setName("User");
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);
			for (int i = 0; i < result.length; i++)
			{
				if (!result[i].getName().toString().contentEquals(myAgent.getAID().toString())){
					System.out.println("Je suis " + myAgent.getAID() + " et j'envoie à " + result[i].getName());
					message.addReceiver(result[i].getName());
				}
			}
		}catch (FIPAException e) {
			e.printStackTrace();
		}
		
		//Envoi du message
		myAgent.send(message);
	}
	
	private String m_message;

}
