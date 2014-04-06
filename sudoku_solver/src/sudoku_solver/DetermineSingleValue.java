package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;

import java.io.Console;
import java.util.ArrayList;

public class DetermineSingleValue extends OneShotBehaviour {

	public DetermineSingleValue(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
//		System.out.println("============ Finding out a single value ============");
		int[][] numberApparitions = new int[9][2];  // frequency & index where we found it 
		for (int i=0; i < 9 ; ++i) {
			numberApparitions[i][0] = 0;
			numberApparitions[i][1] = 0;
		}
		ArrayList<Case> array = m_myAgent.get_m_array();
		// count the occurrences of each possibilities
		for (Case c1 : array) {
//			System.out.println("Case value : "+c1.getValue());
//			System.out.println("Case possibilities : "+c1.getPossibilities());
//			System.out.println("Increment ");
			for (int p : c1.getPossibilities()) {
//				System.out.print(p+", ");
				numberApparitions[p-1][0]++;
				numberApparitions[p-1][1] = array.indexOf(c1);
			}
//			System.out.println();
		}
		// for each possibility occurring only once, its value is set up in the case where it was found
		for (int i=0; i < 9 ; ++i) {
			if (numberApparitions[i][0] == 1) {
//				System.out.println("VALEUR TROUVEE : "+ (i+1) + " A L'IDX "+numberApparitions[i][1]);
//				System.out.println("AVANT L'AFFECTATION");
//				for (Case c : array) {
//					System.out.println("VALUE : "+c.getValue());
//					System.out.println("POS : "+c.getPossibilities());
//				}
				array.set(numberApparitions[i][1], new Case(i+1));
//				System.out.println("APRES L'AFFECTATION");
//				for (Case c : array) {
//					System.out.println("VALUE : "+c.getValue());
//					System.out.println("POS : "+c.getPossibilities());
//				}
			}
		}
		m_myAgent.set_m_array(array);
//		Console console = System.console();
//		String input = console.readLine("Enter input:");
	}
	
	// Members
		private AnaAgent m_myAgent;

}
