package sudoku_solver;

import java.util.List;

public class Case {

	public Case() {
		m_value = 0;
		for (int i = 1; i <= 10; ++i){
			m_possibilities.add(i);
		}
	}

	//Getters & Setters
	
	public int getValue() {return m_value;}
	public void setValue(int m_value) {this.m_value = m_value;}
	public List<Integer> getPossibilities() {return m_possibilities;}
	public void setPossibilities(List<Integer> m_possibilities) {this.m_possibilities = m_possibilities;}
	
	//Members
	private int m_value;
	private List<Integer> m_possibilities;
}
