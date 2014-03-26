package mult;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class MultBehaviour extends WakerBehaviour {
	
	public MultBehaviour(Agent a, long timeout, ACLMessage order) {
		super(a, timeout);
		m_order = order;
	}

	private void sendResult(int res, int op1, int op2, int input, AID receiver) {
		ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
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
			sendFailureMessage(myAgent.getName() + " failed formatting the answer message ", receiver);
		}
		myAgent.send(response);
		System.out.println("Message sent from : " + myAgent.getName() + " to : " + receiver.getName());
	}
	
	
	private void sendFailureMessage(String content, AID receiver){
		ACLMessage failureMessage = new ACLMessage(ACLMessage.FAILURE);
		failureMessage.setContent(content);
		failureMessage.addReceiver(receiver);
		myAgent.send(failureMessage);
	}
	
	

	@Override
	public void onWake() {
			int result;
			int op1 = 0;
			int op2 = 0;
			int input = 0;
			AID requestSender = new AID();
			
			System.out.println("My name is " + myAgent.getName() + " and I've been asked to make a multiplication !");
			
			//Parsing des arguments de la multiplication
			String orderContent = m_order.getContent();
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode jrootNode = mapper.readValue(orderContent, JsonNode.class);
				op1 = jrootNode.path("content").path("numbers").path(0).intValue();
				op2 = jrootNode.path("content").path("numbers").path(1).intValue();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				sendFailureMessage(myAgent.getName() + " failed parsing the request", m_order.getSender());
			}
				
			//Exécution de la requête
			result = op1 * op2;
			
			//Envoi de la réponse
			requestSender = m_order.getSender();
			this.sendResult(result, op1, op2, input, requestSender);
	}
	
	
	//Members
	
	private ACLMessage m_order;
	
}
