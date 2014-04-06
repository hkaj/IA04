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
		ArrayList<Case> array = m_myAgent.get_m_array();
		ArrayList<Integer[]> twoPossCases = new ArrayList<Integer[]>();
		
		// if a case got 2 possibilities we add its index and the 2 possibilities
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
		
		ArrayList<Integer> idxToIgnore = new ArrayList<Integer>();
		for (Integer[] tab1 : twoPossCases) {
			for (Integer[] tab2 : twoPossCases) {
				if ((twoPossCases.indexOf(tab1) != twoPossCases.indexOf(tab2)) && (!idxToIgnore.contains(twoPossCases.indexOf(tab1)))) {
					List<Integer> tab2List = Arrays.asList(tab2);
					tab2List = new ArrayList<Integer>(tab2List);
					tab2List.remove(0);  // because we don't care about the index
					if ((tab2List.contains(tab1[1])) && (tab2List.contains(tab1[2]))) {
						// the 2 cases got the same possibilities
						removePossibilities(array, tab1[0], tab2[0], tab1[1], tab1[2]);
						idxToIgnore.add(twoPossCases.indexOf(tab2));  // so we don't try to remove them again
					}
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
					c.setPossibilities(p);
				}
			}
		
		}
}
