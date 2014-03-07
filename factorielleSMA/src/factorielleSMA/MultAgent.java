package factorielleSMA;
import jade.core.Agent;
import factorielleSMA.MultBehaviour;

public class MultAgent extends Agent {
	
	protected void setup() {
		System.out.println("Hello, my name is " + this.getLocalName());
		addBehaviour(new MultBehaviour(this));
	}
}
