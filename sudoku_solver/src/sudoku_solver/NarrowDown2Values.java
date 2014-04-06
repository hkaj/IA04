package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NarrowDown2Values extends OneShotBehaviour {

	public NarrowDown2Values(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
		int[][] numberApparitions = new int[9][3];  // frequency & index where we found it 
		for (int i=0; i < 9 ; ++i) {
			numberApparitions[i][0] = 0;
			numberApparitions[i][1] = 0;
			numberApparitions[i][2] = 0;
		}
		ArrayList<Case> array = m_myAgent.get_m_array();
		ArrayList<Integer[]> twoPossCases= new ArrayList<Integer[]>();
		// count the occurrences of each possibilities
		for (Case c1 : array) {
			if (c1.getPossibilities().size() == 2) {
				Integer[] tmp = {
						new Integer(array.indexOf(c1)),
						new Integer(c1.getPossibilities().get(0)),
						new Integer(c1.getPossibilities().get(1))
				};
				twoPossCases.add(tmp);
			}
		}
		for (Integer[] tab1 : twoPossCases) {
			for (Integer[] tab2 : twoPossCases) {
				List<Integer> tab2List = Arrays.asList(tab2);
				if ((tab2List.contains(tab1[1])) && (tab2List.contains(tab1[2]))) {
					// the 2 cases got the same possibilities
					removePossibilities(array, tab1[0].intValue(), tab2[0].intValue(), tab1[1], tab1[2]);
				}
			}
		}
		m_myAgent.set_m_array(array);
	}
	
	// Members
		private AnaAgent m_myAgent;
		
		// Remove p1 & p2 from the possibilities of every case except ex1 & ex2.
		private void removePossibilities(ArrayList<Case> array, int ex1, int ex2, Integer p1, Integer p2) {
			for (Case c : array) {
				if ((array.indexOf(c) != ex1) && (array.indexOf(c) != ex2)) {
					ArrayList<Integer> p = c.getPossibilities();
					p.remove(p1);
					p.remove(p2);
				}
			}
		
		}

}
