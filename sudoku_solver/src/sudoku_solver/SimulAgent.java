package sudoku_solver;

import jade.core.Agent;

public class SimulAgent extends Agent {
	
	@Override
	protected void setup() {
		this.addBehaviour(new SimRecMsgBehaviour(this));
		super.setup();
	}

}
