package sudoku_solver;

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
		
		EnvAgent agent = (EnvAgent) myAgent;
		AID anaAgentId = (AID) m_listOfAnalAgentmessage.getAllReplyTo().next();
		agent.setConnectionAnalyseStructure(anaAgentId);
		
		//Envoi de la première requête d'envoi
		myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent, anaAgentId));
		
	}
	
	//Members
	private ACLMessage m_listOfAnalAgentmessage;

}
