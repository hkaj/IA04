package sudoku_solver;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jade.core.Agent;

public class EnvAgent extends Agent {
	
	
	public EnvAgent(String filename) throws FileNotFoundException, IOException {
		super();
		m_pcs = new PropertyChangeSupport(this);
		
		BufferedReader br = new BufferedReader(new FileReader("file.txt"));
	    try {
	        String line = br.readLine();
	        int i = 0;
	        
	        try{
	        	while (line != null && i < 10) {
	        	
		        	for (int j = 0; j < 10; j++){
		        		m_sudoku[i][j] = new Case(Character.getNumericValue(line.charAt(j)));
		        	}
		   
		            line = br.readLine();
		            ++i;
	        	}
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("Iteration " + i);
	        }
	    } finally {
	        br.close();
	    }
	    
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
	}

	//Events
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	//Getters & Setters
	public Case[][] getSudoku() {return m_sudoku;}
	public void setSudoku(Case[][] m_sudoku) {this.m_sudoku = m_sudoku;}

	//Members
	private Case m_sudoku[][];
	private PropertyChangeSupport m_pcs;
}
