package ontologie;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class GeodataReceiveMessageBehaviour extends CyclicBehaviour {

	public GeodataReceiveMessageBehaviour(Agent agent) {
		super(agent);
	}
	
	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null){
			if ((message.getPerformative() == ACLMessage.QUERY_REF) && (message.getLanguage().equals("SPARQL"))){
				myAgent.addBehaviour(new GeodataSparqlProcessBehaviour(myAgent, message));
			} else
				System.out.println("Received unexpected message");
		}
		else {
			block();
		}
	}

}
