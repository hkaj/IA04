package ontologie;

import jade.core.Agent;

public class PropagateSparql extends Agent {
	
	@Override
	public void setup(){
		addBehaviour(new ReceiveSparqlBehaviour(this));
	}

}
