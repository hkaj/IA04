package ontologie;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class InterfaceReceiveMessageBehaviour extends CyclicBehaviour {

	public InterfaceReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		
		if (message != null){
			if (message.getPerformative() == ACLMessage.PROPAGATE){
				myAgent.addBehaviour(new InterfaceFormatRequestBehaviour(myAgent, message));
			} else if (message.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new InterfaceShowKBAnswer(myAgent,message));
			}
		}
		else
			block();
	}

}
