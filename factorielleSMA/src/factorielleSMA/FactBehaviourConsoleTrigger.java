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
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class FactBehaviourConsoleTrigger extends Behaviour {

	public FactBehaviourConsoleTrigger(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		//Reception of the message
		ACLMessage message = myAgent.blockingReceive();
		int nb = 0;
		if (message != null)
		{
			String messageContent = message.getContent();
			ObjectMapper mapper = new ObjectMapper();
				try {
					//Parsing received JSON message
					JsonNode jrootNode = mapper.readValue(messageContent, JsonNode.class);
					nb = jrootNode.path("content").path("numbers").path(0).intValue();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
			
			if (nb > 0)
			{
				System.out.println("Calculation of " + nb + " factorial value");
				ACLMessage message2 = new ACLMessage(ACLMessage.REQUEST);

				ObjectMapper writerMapper = new ObjectMapper();
				try {
					//Construction of the message for MultAgent
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
				
				message2.addReceiver(searchReceiver());
				//System.out.println(message2);
				myAgent.send(message2);
			}
			else
				System.out.println("No result !");
		}
		else
			block();

	}
	
	
	private AID searchReceiver(){
		AID aid = new AID();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Operations");
		sd.setName("Multiplication");
		template.addServices(sd);
//		System.out.println(template);
		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);
//			System.out.println("number of mult agents: " + result.length);
			if (result.length > 0){
				int indice = ((int)Math.random() * result.length);
				aid = result[indice].getName();
			}
		}catch (FIPAException e)
		{
			e.printStackTrace();
		}
		
		return aid;
	}

	@Override
	public boolean done() {
		return true;
	}

}
