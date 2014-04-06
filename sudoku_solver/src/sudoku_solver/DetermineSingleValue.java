package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;

public class DetermineSingleValue extends OneShotBehaviour {

	public DetermineSingleValue(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
		int[][] numberApparitions = new int[9][2];  // frequency & index where we found it 
		for (int i=0; i < 10 ; ++i) {
			numberApparitions[i][0] = 0;
			numberApparitions[i][1] = 0;
		}
		ArrayList<Case> array = m_myAgent.get_m_array();
		// count the occurrences of each possibilities
		for (Case c1 : array) {
			for (int p : c1.getPossibilities()) {
				numberApparitions[p-1][0]++;
				numberApparitions[p-1][1] = array.indexOf(c1);
			}
		}
		// for each possibility occurring only once, its value is set up in the case where it was found
		for (int i=0; i < 10 ; ++i) {
			if (numberApparitions[i][0] == 1) {
				array.set(numberApparitions[i][1], new Case(i+1));
			}
		}
		m_myAgent.set_m_array(array);
	}
	
	// Members
		private AnaAgent m_myAgent;

}
