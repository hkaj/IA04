package ontologie;

import jade.core.Agent;

public class InterfaceAgent extends Agent {

	public InterfaceAgent() {
		
	}
	
	@Override
	public void setup(){
		addBehaviour(new InterfaceReceiveMessageBehaviour(this));
	}

}