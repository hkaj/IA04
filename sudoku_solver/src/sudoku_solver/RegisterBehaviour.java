package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RegisterBehaviour extends OneShotBehaviour {

	public RegisterBehaviour(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
		ACLMessage registration = new ACLMessage(ACLMessage.SUBSCRIBE);
		registration.addReceiver(m_myAgent.get_m_simulAgent());
		myAgent.send(registration);
		myAgent.addBehaviour(new AnaRecMsgBehaviour(m_myAgent));
	}

	// Members
	private AnaAgent m_myAgent;

}
