package factorielleSMA;
import factorielleSMA.FactBehaviour;
import jade.core.AID;
import jade.core.Agent;

public class FactAgent extends Agent {
	
	private AID multAgent = new AID("multAgent", AID.ISLOCALNAME);
	
	protected void setup() {
		System.out.println("Hello, my name is " + this.getLocalName());
		addBehaviour(new FactBehaviour(this));
		addBehaviour(new FactBehaviourConsoleTrigger(this));
	}
	
	public AID getMultAgent() {
		return this.multAgent;
	}
}
