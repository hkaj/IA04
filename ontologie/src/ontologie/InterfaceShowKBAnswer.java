package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InterfaceShowKBAnswer extends OneShotBehaviour {

	public InterfaceShowKBAnswer(Agent myAgent, ACLMessage message) {
		m_message = message;
	}

	@Override
	public void action() {
		System.out.println("The answer from KB is : " + m_message.getContent());
	}
	
	//Members
	private ACLMessage m_message;

}
