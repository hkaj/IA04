package ontologie;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class KBSendResultBehaviour extends OneShotBehaviour {


	public KBSendResultBehaviour(Agent a, String content, ArrayList<AID> receivers) {
		super(a);
		m_messageContent = content;
		m_receivers = receivers;
	}

	@Override
	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		for (AID receiver : m_receivers)
			message.addReceiver(receiver);
		message.setContent(m_messageContent);
		myAgent.send(message);
	}
	
	//Members
	private String m_messageContent;
	private ArrayList<AID> m_receivers;

}
