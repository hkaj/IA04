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
		
		String msg = m_message.getContent();
		String[] data;
		if (msg.contains("§")){
			data = msg.split("§");
		} else
			data = null;

		ObjectMapper writerMapper = new ObjectMapper();
		try{
			StringWriter sw = new StringWriter();
			HashMap<String, String> content = new HashMap<>();
			
			if (data == null){
				// Seul l'ID est dans le message
				content.put("subject", m_message.getContent());
			} else if (data.length == 3) {
				// Trois informations dans le message : type/key1/key2
				if (data[0].equals("getSubject")){
					// On veut les assertions pour une propri�t� et une valeur donn�e
					content.put("property", data[1]);
					content.put("object", data[2]);
				} else if (data[0].equals("getObject")) {
					// On veut les assertions pour un id et une propri�t� donn�s
					content.put("subject", data[1]);
					content.put("property", data[2]);
				}
			}
			
			HashMap<String, Object> messageContent = new HashMap<>();
			messageContent.put("content", content);
			writerMapper.writeValue(sw, messageContent);
			request.setContent(sw.toString());
//			System.out.println("Request content : " + request.getContent());

			myAgent.send(request);
		}catch (Exception e){
			e.printStackTrace();
		}
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
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return aid;
	}
	//Members
	private ACLMessage m_message;
}
