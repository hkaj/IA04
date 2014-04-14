package ontologie;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class KBSendResultBehaviour extends OneShotBehaviour {


	public KBSendResultBehaviour(Agent a, String content, ArrayList<AID> receivers) {
		super(a);
		m_messageContent = content;
		m_receivers = receivers;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}
	
	//Members
	private String m_messageContent;
	private ArrayList<AID> m_receivers;

}
