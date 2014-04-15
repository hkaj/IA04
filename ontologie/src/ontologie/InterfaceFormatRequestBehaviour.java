package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InterfaceFormatRequestBehaviour extends OneShotBehaviour {

	public InterfaceFormatRequestBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}
	
	//Members
	private ACLMessage m_message;

}
