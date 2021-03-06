package factorielleSMA;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class MultAgent extends Agent {
	
	protected void setup() {
		System.out.println("Hello, my name is " + this.getLocalName());
		addBehaviour(new MultReceiveMessageBehaviour(this));
		
		//Registering to the AMS list
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Operations");
		sd.setName("Multiplication");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch (FIPAException e){
			e.printStackTrace();
		}
		
	}
}
