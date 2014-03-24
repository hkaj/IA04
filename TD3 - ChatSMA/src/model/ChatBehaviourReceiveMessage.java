package model;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ChatBehaviourReceiveMessage extends CyclicBehaviour {

	public ChatBehaviourReceiveMessage(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		
		if (message != null){
			if (message.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new ChatBehaviourMessageProcessing(myAgent, message));
			}
		}
			
	}

}
