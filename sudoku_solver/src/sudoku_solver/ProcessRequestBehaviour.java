package sudoku_solver;

import jade.core.behaviours.SequentialBehaviour;

public class ProcessRequestBehaviour extends SequentialBehaviour {

	public ProcessRequestBehaviour(AnaAgent a) {
		super(a);
		m_myAgent = a;
		this.addSubBehaviour(new RemoveFoundValues(m_myAgent));
		this.addSubBehaviour(new DetermineSingleValue(m_myAgent));
		this.addSubBehaviour(new NarrowDown2Values(m_myAgent));
	}
	
	// Members
	private AnaAgent m_myAgent;

}
