package sudoku_solver;

import jade.core.behaviours.SequentialBehaviour;

public class ProcessRequestBehaviour extends SequentialBehaviour {

	public ProcessRequestBehaviour(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}
	
	// Members
	private AnaAgent m_myAgent;

}
