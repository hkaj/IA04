package factorielleSMA;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class FactBehaviourConsoleTrigger extends Behaviour {

	public FactBehaviourConsoleTrigger(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		int nb = 0;
		if (message != null)
		{
			String messageContent = message.getContent();
			ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode jrootNode = mapper.readValue(messageContent, JsonNode.class);
					nb = jrootNode.path("content").path("numbers").path(0).intValue();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			if ((nb > 0) && message.getSender().getLocalName() == "console")
			{
				ACLMessage message2 = new ACLMessage(ACLMessage.REQUEST);

				ObjectMapper writerMapper = new ObjectMapper();
				try {
					Map<String, Object> numbersMap = new HashMap<String, Object>();
					List<Object> list = new ArrayList<Object>();
					list.add(1);
					list.add(nb);
					numbersMap.put("numbers", list);
					Map<String, Object> contentMap = new HashMap<String, Object>();
					contentMap.put("content", numbersMap);
					StringWriter sw = new StringWriter();
					writerMapper.writeValue(sw, contentMap);
					message2.setContent(sw.toString());
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				message2.addReceiver(new AID("factAgent",AID.ISLOCALNAME));
				System.out.println(message2);
				myAgent.send(message2);
			}
			else
				System.out.println("No result !");
		}
		else
			block();

	}

	@Override
	public boolean done() {
		return true;
	}

}
