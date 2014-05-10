package ontologie;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class KBReceiveMessageBehaviour extends CyclicBehaviour {
	
	public KBReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null){
			if ((message.getPerformative() == ACLMessage.QUERY_REF) && (message.getLanguage().equals("BRUT"))){
				myAgent.addBehaviour(new KBRequestProcessBehaviour(myAgent, message));
			} else if ((message.getPerformative() == ACLMessage.QUERY_REF) && (message.getLanguage().equals("SPARQL"))){
				myAgent.addBehaviour(new KBSparqlProcessBehaviour(myAgent, message));
			} else
				System.out.println("Received unexpected message");
		}
		else {
			block();
		}
	}

}
