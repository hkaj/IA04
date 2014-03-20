package factorielleSMA;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.Thread;

public class MultBehaviour extends CyclicBehaviour {
	
	public MultBehaviour(Agent a) {
		super(a);
	}
	
	private void sendResult(int res, int op1, int op2, AID receiver) {
		ACLMessage response = new ACLMessage(ACLMessage.INFORM);
		response.addReceiver(receiver);

		ObjectMapper writerMapper = new ObjectMapper();
		try {
			Map<String, Object> numbersMap = new HashMap<String, Object>();
			List<Object> list = new ArrayList<Object>();
			List<Object> resultList = new ArrayList<Object>();
			list.add(op1);
			list.add(op2);
			resultList.add(res);
			numbersMap.put("numbers", list);
			numbersMap.put("result", resultList);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", numbersMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			response.setContent(sw.toString()); 

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		myAgent.send(response);
	}

	public void action() {
			int result;
			int op1 = 0;
			int op2 = 0;
			AID requestSender = new AID(); 
			
			ACLMessage order = myAgent.blockingReceive();
			
			if (order != null) {
				System.out.println("My name is " + myAgent.getName() + " and I've been asked to make a multiplication !");
				
				//Parsing des arguments de la multiplication
				String orderContent = order.getContent();
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode jrootNode = mapper.readValue(orderContent, JsonNode.class);
					op1 = jrootNode.path("content").path("numbers").path(0).intValue();
					op2 = jrootNode.path("content").path("numbers").path(1).intValue();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
					
				//Exécution de la requête
				result = op1 * op2;
				
				//On s'endort avant d'envoyer la r�ponse
				try {
					int wait_time = 500 + (int)(Math.random() * (10000 - 500 + 1)); 
					System.out.println("let's wait " + wait_time + "ms.");
					Thread.sleep(wait_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//Envoi de la réponse
				requestSender = order.getSender();
				this.sendResult(result, op1, op2, requestSender);
			}
	}
	
}
