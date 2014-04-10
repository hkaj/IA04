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
		
		//Gestion des différents types de message
		if (message != null){
			if (message.getPerformative() == ACLMessage.INFORM){
				//Message provenant de Simul pour les résultats d'Analyse : on met à jour le sudoku
				myAgent.addBehaviour(new EnvUpdateSudokuBehaviour(myAgent, message));
			}else if (message.getPerformative() == ACLMessage.INFORM_REF){
				//Message provenant de Simul pour une registration d'Analyse : on met à jour la table de correspondance
				myAgent.addBehaviour(new EnvCreateLinkBehaviour(myAgent, message));
			}else{
				System.out.println("Message with performative " + message.getPerformative() + " is not processed");
			}
		}
		else
			block();
	}

}
