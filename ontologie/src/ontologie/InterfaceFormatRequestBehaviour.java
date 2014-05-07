package ontologie;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InterfaceFormatRequestBehaviour extends OneShotBehaviour {

	public InterfaceFormatRequestBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
	}

	@Override
	public void action() {
		
		ACLMessage request = new ACLMessage(ACLMessage.QUERY_REF);
		request.addReceiver(searchKnowledgeBase());
		
		String fuck = m_message.getContent();
		String[] data;
		if (fuck.contains("§")){
			data = fuck.split("§");
		} else
			data = null;
//		System.out.println("1 " + data[0]);
//		System.out.println("2 " +data[2]);
		ObjectMapper writerMapper = new ObjectMapper();
		try{
			StringWriter sw = new StringWriter();
			HashMap<String, String> content = new HashMap<>();
			
			if (data == null){
				// Seul l'ID est dans le message
				content.put("id", m_message.getContent());
			} else if (data.length == 3) {
				// Trois informations dans le message : type/key1/key2
				
				if (data[0].equals("prop")){
					// On veut les assertions pour une propri�t� et une valeur donn�e
					content.put("prop-name", data[1]);
					content.put("prop-value", data[2]);
				} else if (data[0].equals("assert")) {
					// On veut les assertions pour un id et une propri�t� donn�s
					content.put("id", data[1]);
					content.put("prop-name", data[2]);
				}
			}
			
			HashMap<String, Object> messageContent = new HashMap<>();
			messageContent.put("content", content);
			
			writerMapper.writeValue(sw, messageContent);
			request.setContent(sw.toString());
			
			System.out.println("Request content : " + request.getContent());

			myAgent.send(request);
		}catch (Exception e){
			e.printStackTrace();
		}
		
//		if (data.length == 3) {
//			if (data[0] != "") {
//				
//			}
//		} else {
//			System.out.println("The request sent through the console isn't correctly formatted.");
//		}

	}
	
	private AID searchKnowledgeBase() {
		AID aid = new AID();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();

		sd.setType("Knowledge Base");
		template.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);
			if (result.length > 0){
				aid = result[0].getName();
			}
		}catch (FIPAException e) {
			e.printStackTrace();
		}
		return aid;
	}

	//Members
	private ACLMessage m_message;

}
