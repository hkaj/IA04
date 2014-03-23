package factorielleSMA;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

import java.lang.Math;

public class FactBehaviour extends OneShotBehaviour {
	
	public FactBehaviour(Agent a, ACLMessage order) {
		super(a);
		m_order = order;
	}

	public void action() {
		int nb = 0;
		int res = 1;
		int input = 0;
		
		//Parsing received JSON message from MultAgent
		String messageContent = m_order.getContent();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jrootNode = mapper.readValue(messageContent, JsonNode.class);
			nb = jrootNode.path("content").path("numbers").path(1).intValue();
			res = jrootNode.path("content").path("result").path(0).intValue();
			input = jrootNode.path("meta").path("input").intValue();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	
		if (--nb > 0) {
			this.sendOrder(res, nb, input);
		} else {
			System.out.println("The result of " + input + "! is: " + res);
		}
	}
	
	private void sendOrder(int nb1, int nb2, int input) {
		// instantiating the message (request type)
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(searchReceiver());

		ObjectMapper writerMapper = new ObjectMapper();
		try {
			Map<String, Object> numbersMap = new HashMap<String, Object>();
			List<Object> list = new ArrayList<Object>();
			list.add(nb1);
			list.add(nb2);
			numbersMap.put("numbers", list);
			Map<String, Integer> metaMap = new HashMap<String, Integer>();
			metaMap.put("input", input);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", numbersMap);
			contentMap.put("meta", metaMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			order.setContent(sw.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// sending the calculus request
		myAgent.send(order);
	}
	
	
	private AID searchReceiver(){
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
	
	//Members
	private ACLMessage m_order;
}
