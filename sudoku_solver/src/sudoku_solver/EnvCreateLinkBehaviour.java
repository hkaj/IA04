package sudoku_solver;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvCreateLinkBehaviour extends OneShotBehaviour {

	public EnvCreateLinkBehaviour() {
		// TODO Auto-generated constructor stub
	}

	public EnvCreateLinkBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_listOfAnalAgentmessage = message;
	}

	@Override
	public void action() {
		String content = m_listOfAnalAgentmessage.getContent();
		ObjectMapper mapper = new ObjectMapper();
		
		try{
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			List<AID> listOfAgents = mapper.readValue(jrootNode.path("content").path("AIDs").textValue(), new TypeReference<List<AID>>(){});
			EnvAgent agent = (EnvAgent) myAgent;
			for(Iterator<AID> it = listOfAgents.iterator(); it.hasNext();){
				agent.setConnectionAnalyseStructure(it.next());
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent));
		
	}
	
	//Members
	private ACLMessage m_listOfAnalAgentmessage;

}
