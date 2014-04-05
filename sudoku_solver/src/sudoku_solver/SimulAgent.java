package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Map;

public class SimulAgent extends Agent {
	
	public SimulAgent() {
		this.m_envAgent = new AID("EnvAgent", true);
	}
	@Override
	protected void setup() {
		this.addBehaviour(new SimRecMsgBehaviour(this));
		super.setup();
	}
	
	// Members
	ArrayList<AID> m_anaAgents;
	AID m_envAgent;
	Map<AID, ArrayList<Case>> m_result;
}
