package factorielleSMA;
import factorielleSMA.FactBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class FactAgent extends Agent {
	
	private AID multAgent = new AID("multAgent", AID.ISLOCALNAME);
	
	protected void setup() {
		System.out.println("Hello, my name is " + this.getLocalName());
		addBehaviour(new FactBehaviourConsoleTrigger(this));
		addBehaviour(new FactBehaviour(this));
		
		//Registering to the AMS list
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Operations");
		sd.setName("Factorielle");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch (FIPAException e){
			e.printStackTrace();
		}
	}
	
	public AID getMultAgent() {
		return this.multAgent;
	}
}
