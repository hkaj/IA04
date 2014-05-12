package ontologie;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveGeoSparqlBehaviour extends CyclicBehaviour {

	public ReceiveGeoSparqlBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null){
			if ((message.getPerformative() == ACLMessage.PROPAGATE) && (message.getLanguage().equals("SPARQL"))) {
				myAgent.addBehaviour(new FormatGeoSparqlRequestBehaviour(myAgent, message));
			}  else if (message.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new SparqlShowKBAnswer(myAgent,message));
			}
		}
		else
			block();
	}

}
