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
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class FactBehaviour extends Behaviour {
	
	public FactBehaviour(Agent a) {
		super(a);
	}

	public void action() {
		int nb = 0;
		int res = 1;

		ACLMessage request = myAgent.blockingReceive();
		if ((request != null) && (request.getSender().getLocalName() != "console")) {
		String messageContent = request.getContent();
		ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode jrootNode = mapper.readValue(messageContent, JsonNode.class);
				nb = jrootNode.path("content").path("numbers").path(0).intValue();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		int tmp = nb;

		ACLMessage response;
		while (tmp > 0) {
			this.sendOrder(res, tmp);
			response = myAgent.blockingReceive();
			if (response != null) {
				String responseContent = response.getContent();
				System.out.println("gfh" + response.getContent());
				ObjectMapper mapper = new ObjectMapper();
					try {
						JsonNode jrootNode = mapper.readValue(responseContent, JsonNode.class);
						res = jrootNode.path("content").path("numbers").path(0).intValue();
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				tmp -= 1;
			}
		}
		// printing the result
		System.out.println(nb + "! = " + res);
	}
	
	private void sendOrder(int nb1, int nb2) {
		// instantiating the message (request type)
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(new AID("multAgent", AID.ISLOCALNAME)); // we can use myAgent.multAgent

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
			System.out.println("sending an order");
			System.out.println(sw);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		// sending the calculus request
		myAgent.send(order);
	}
	
	public boolean done() {
		return true;
	}

}
