package model;

import view.ChatWindow;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ChatAgent extends Agent {

	@Override
	protected void setup() {
		//Génération de la Fenetre
		ChatWindow chatWindow = new ChatWindow("", this);
		addBehaviour(new ChatBehaviourReceiveMessage(this));
		addBehaviour(new ChatBehaviourSendMessage(this));
		
		//Registering to the AMS list
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Chat");
				sd.setName("User");
				dfd.addServices(sd);
				try{
					DFService.register(this, dfd);
				} catch (FIPAException e){
					e.printStackTrace();
				}
	}
}
