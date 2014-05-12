package ontologie;

import jade.core.Agent;

public class PropagateGeoSparqlAgent extends Agent {
	
	@Override
	public void setup(){
		addBehaviour(new ReceiveGeoSparqlBehaviour(this));
	}


}
