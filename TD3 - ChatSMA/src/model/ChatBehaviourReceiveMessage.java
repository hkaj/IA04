package model;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class ChatBehaviourReceiveMessage extends CyclicBehaviour {

	public ChatBehaviourReceiveMessage(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		myAgent.addBehaviour(new ChatBehaviourSendMessage(myAgent));
	}

}
