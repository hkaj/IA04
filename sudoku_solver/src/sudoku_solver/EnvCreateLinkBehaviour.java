package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvCreateLinkBehaviour extends OneShotBehaviour {

	public EnvCreateLinkBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_listOfAnalAgentmessage = message;
	}

	@Override
	public void action() {
		
		//Creating the link between Analyse Agent and Zone
		EnvAgent agent = (EnvAgent) myAgent;
		AID anaAgentId = (AID) m_listOfAnalAgentmessage.getAllReplyTo().next();
		agent.setConnectionAnalyseStructure(anaAgentId);
		
		//Envoi de la première requête d'envoi
		myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent, anaAgentId, m_listOfAnalAgentmessage.getSender()));
		
	}
	
	//Members
	private ACLMessage m_listOfAnalAgentmessage;

}
