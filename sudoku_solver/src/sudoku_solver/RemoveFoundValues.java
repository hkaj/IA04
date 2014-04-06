package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;

import java.io.Console;
import java.util.ArrayList;

public class RemoveFoundValues extends OneShotBehaviour {

	public RemoveFoundValues(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
//		System.out.println("Removing found values from possibilities");
		int value = 0;
		ArrayList<Case> array = m_myAgent.get_m_array();
		for (Case c1 : array) {
			value = c1.getValue();
			if (value != 0) {
				for (Case c2 : array) {
					c2.RemovePossibility(value);
				}
			}
		}
		m_myAgent.set_m_array(array);
	}

	// Members
		private AnaAgent m_myAgent;

}
