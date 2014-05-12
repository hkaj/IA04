package ontologie;

import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class FormatGeoSparqlRequestBehaviour extends OneShotBehaviour {

	public FormatGeoSparqlRequestBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
	}
	
	@Override
	public void action() {
		ACLMessage request = new ACLMessage(ACLMessage.QUERY_REF);
		request.addReceiver(searchKnowledgeBase());
		request.setLanguage(m_message.getLanguage());

		ObjectMapper writerMapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try{
			HashMap<String, String> content = new HashMap<>();
			content.put("request", m_message.getContent());
			writerMapper.writeValue(sw, content);
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
		sd.setType("Distant Knowledge Base");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this.myAgent, template);
			if (result.length > 0){
				aid = result[0].getName();
			} else {
				System.out.println("No Distant Knowledge Base found");
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return aid;
	}
	
	//Members
	private ACLMessage m_message;

}
