package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class KBInitialiseKnowledgeBase extends OneShotBehaviour {

	public KBInitialiseKnowledgeBase(Agent a, String rulesPath, String assertionsPath) {
		super(a);
		m_rulesPath = rulesPath;
		m_assertionsPath = assertionsPath;
		m_agent = (KnowledgeBaseAgent) a;
	}

	@Override
	public void action() {
		if (m_rulesPath != null){
			m_agent.getModel().read("file:" + m_rulesPath);
		}
		
		if (m_assertionsPath != null){
			m_agent.getModel().read("file:" + m_assertionsPath, null, "TURTLE");
		}

	}
	
	//Members
	private String m_rulesPath;
	private String m_assertionsPath;
	private KnowledgeBaseAgent m_agent;

}
