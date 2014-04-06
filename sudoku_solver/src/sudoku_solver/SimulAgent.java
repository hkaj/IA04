package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulAgent extends Agent {
	
	public SimulAgent() {
		super();
		this.m_envAgent = new AID("EnvAgent", AID.ISLOCALNAME);
		this.m_anaAgents = new ArrayList<AID>();
		this.m_result = new HashMap<String, ArrayList<Case>>();
	}
	@Override
	protected void setup() {
		this.addBehaviour(new SimRecMsgBehaviour(this));
		super.setup();
	}
	
	// Members
	private ArrayList<AID> m_anaAgents;
	private AID m_envAgent;
	private Map<String, ArrayList<Case>> m_result;
	
	public ArrayList<AID> get_m_anaAgents() {
		return m_anaAgents;
	}
	public void add_m_anaAgents(AID aid) {
		this.m_anaAgents.add(aid);
	}
	public AID get_m_envAgent() {
		return m_envAgent;
	}
	public void set_m_envAgent(AID m_envAgent) {
		this.m_envAgent = m_envAgent;
	}
	public Map<String, ArrayList<Case>> get_m_result() {
		return m_result;
	}
	public void put_m_result(AID aid, ArrayList<Case> list) {
		this.m_result.put(aid.getName(), list);
	}
	public void clear_m_result() {
		this.m_result.clear();
	}
}
