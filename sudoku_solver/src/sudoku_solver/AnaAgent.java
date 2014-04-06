package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;

public class AnaAgent extends Agent {
	
	public AnaAgent() {
		super();
		this.m_array = new ArrayList<Case>(9);
		this.m_simulAgent = new AID("SimAgent", AID.ISLOCALNAME);
	}

	@Override
	protected void setup() {
		this.addBehaviour(new RegisterBehaviour(this));
		super.setup();
	}
	
	// Members
	private AID m_simulAgent;
	private ArrayList<Case> m_array;

	public AID get_m_simulAgent() {
		return m_simulAgent;
	}

	public void set_m_simulAgent(AID m_simulAgent) {
		this.m_simulAgent = m_simulAgent;
	}

	public ArrayList<Case> get_m_array() {
		return m_array;
	}

	public void set_m_array(ArrayList<Case> m_array) {
		this.m_array = m_array;
	}
}
