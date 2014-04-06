package sudoku_solver;

import java.util.ArrayList;

public class Case {

	public Case() {
		m_value = 0;
		m_possibilities = new ArrayList<Integer>();
		for (int i = 1; i <= 9; ++i){
			m_possibilities.add(i);
		}
	}
	
	
	public Case(int content){
		m_value = content;
		m_possibilities = new ArrayList<Integer>();
		if (content == 0){
			for (int i = 1; i <= 9; ++i){
				m_possibilities.add(i);
			}
		}
	}

	//Getters & Setters
	
	public int getValue() {return m_value;}
	public void setValue(int m_value) {this.m_value = m_value;}
	public ArrayList<Integer> getPossibilities() {return m_possibilities;}
	public void setPossibilities(ArrayList<Integer> m_possibilities) {this.m_possibilities = m_possibilities;}
	public void RemovePossibility(int idx) {m_possibilities.remove(new Integer(idx));}
	//Members
	private int m_value;
	private ArrayList<Integer> m_possibilities;
}
