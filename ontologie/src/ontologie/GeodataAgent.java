package ontologie;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class GeodataAgent extends Agent {	
	
	@Override
	protected void setup() {
		
		registerToAMS();
		
		//Behaviour de réception des messages
		addBehaviour(new GeodataReceiveMessageBehaviour(this));
	}
	
	
	private void registerToAMS() {
		//Registering to the AMS list
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Distant Knowledge Base");
		sd.setName("GeoAgent");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch (FIPAException e){
			e.printStackTrace();
		}		
	}

}
