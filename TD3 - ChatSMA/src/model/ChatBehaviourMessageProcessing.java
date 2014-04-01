package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			
			Pattern p = Pattern.compile("([1-9][0-9]*)\\p{Blank}*x\\p{Blank}*([1-9][0-9]*)");
			Matcher m = p.matcher(message);
			
			if (m.matches()){
				sendOrderToMult(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
			}
		}
	}
	
	
	private void sendOrderToMult(int nb1, int nb2) {
		// instantiating the message (request type)
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(searchMultReceiver());

		ObjectMapper writerMapper = new ObjectMapper();
		try {
			Map<String, Object> numbersMap = new HashMap<String, Object>();
			List<Object> list = new ArrayList<Object>();
			list.add(nb1);
			list.add(nb2);
			numbersMap.put("numbers", list);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", numbersMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			order.setContent(sw.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// sending the calculus request
		myAgent.send(order);
	}
	
	
	private AID searchMultReceiver(){
		AID aid = new AID();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();

		sd.setType("Operations");
		sd.setName("Multiplication");
		template.addServices(sd);

		// Choix aléatoire de l'agent multiplicateur
		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);
			if (result.length > 0){
				int indice = (int)(Math.random() * result.length);
				aid = result[indice].getName();
			}
		}catch (FIPAException e) {
			e.printStackTrace();
		}
		return aid;
	}
	
	ACLMessage m_message;

}
