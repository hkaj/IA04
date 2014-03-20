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
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class FactBehaviourConsoleTrigger extends OneShotBehaviour {

	public FactBehaviourConsoleTrigger(Agent a, ACLMessage order) {
		super(a);
		m_order = order;
	}

	@Override
	public void action() {		
		int nb = 0;
		System.out.println("J'ai lancé mon traitement de behaviour");
		String messageContent = m_order.getContent();
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
			myAgent.send(message2);
		}
		else
			System.out.println("No result !");

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
	
	//Members
	private ACLMessage m_order;

}
