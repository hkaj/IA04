package mult;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MultReceiveMessageBehaviour extends CyclicBehaviour {

	public MultReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage order = myAgent.receive();
		
		if (order != null) {
			if (order.getPerformative() == ACLMessage.REQUEST)
			{
				//Calcul du temps de sleep de l'agent Mult avant d'effectuer son action
				long wait_time = 500 + (long)(Math.random() * (10000 - 500 + 1)); 
				System.out.println("MultAgent will wait " + wait_time + " ms before performing multiplication");
				
				myAgent.addBehaviour(new MultBehaviour(myAgent, wait_time, order));
			} else {
				System.out.println("Invalid request sent to a multiplicator agent.");
			}
		}

	}

}
