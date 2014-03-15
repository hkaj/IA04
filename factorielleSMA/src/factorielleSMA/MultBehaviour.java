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

public class MultBehaviour extends CyclicBehaviour {
	
	public MultBehaviour(Agent a) {
		super(a);
	}
	
	private void sendResult(int res) {
		ACLMessage response = new ACLMessage(ACLMessage.INFORM);
		response.addReceiver( new AID("factAgent", AID.ISLOCALNAME));

		ObjectMapper writerMapper = new ObjectMapper();
		try {
			Map<String, Object> numbersMap = new HashMap<String, Object>();
			List<Object> list = new ArrayList<Object>();
			list.add(res);
			numbersMap.put("numbers", list);
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", numbersMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			response.setContent(sw.toString()); 
			System.out.println("computed this ->");
			System.out.println(response.getContent());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
//		response.setContent(res);
		myAgent.send(response);
	}

	public void action() {
			int result;
			int op1 = 0;
			int op2 = 0;
			ACLMessage order = myAgent.blockingReceive();
			if (order != null) {
				System.out.println(
					myAgent.getName() + " says : I received this -> \n" + order + "\nContent :\n" + order.getContent()
				);
				
				//String[] ops = {};
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
				result = op1 * op2;
				this.sendResult(result);
			}
	}
	
}
