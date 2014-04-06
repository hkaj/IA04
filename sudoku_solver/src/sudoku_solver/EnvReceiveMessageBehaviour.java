package sudoku_solver;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvReceiveMessageBehaviour extends CyclicBehaviour {

	public EnvReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		
		if (message != null){
			if (message.getPerformative() == ACLMessage.INFORM){
				myAgent.addBehaviour(new EnvUpdateSudokuBehaviour(myAgent, message));
			}else if (message.getPerformative() == ACLMessage.INFORM_REF){
				myAgent.addBehaviour(new EnvCreateLinkBehaviour(myAgent, message));
			}else{
				System.out.println("Message with performative " + message.getPerformative() + " is not processed");
			}
		}
	}

}
