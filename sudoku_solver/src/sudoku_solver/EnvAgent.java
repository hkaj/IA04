package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EnvAgent extends Agent {
	
	private enum Structure {LINE, COLUMN, SQUARE};
	
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
		addBehaviour(new EnvReceiveMessageBehaviour(this));		
	}

	//Events
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	//To implement in Behaviour
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	//Getters & Setters
	public Case[][] getSudoku() {return m_sudoku;}
	public void setSudoku(Case[][] m_sudoku) {this.m_sudoku = m_sudoku;}
	
	
	//Changer les informations d'une case
	private void changeSudokuCase (String analyseId, Case cas) {
		//TODO
		
		firePropertyChange("Case_changed", null, cas);
	}
	
	
	//Gestion de la table de correspondance entre AgentAnalyse et ligne, colonne et carré
	public void setConnectionAnalyseStructure (AID analyseId) {
		int numberOfEntries = EnvironnementAnalyseCorresp.getNumberOfEntries();
		if(numberOfEntries < 27){
			Structure type;
			if (numberOfEntries < 9)
				type = Structure.LINE;
			else if (numberOfEntries < 18)
				type = Structure.COLUMN;
			else 
				type = Structure.SQUARE;
			connectionArray.put(analyseId, new EnvironnementAnalyseCorresp(numberOfEntries%9, type, analyseId));
		}
		else
			System.out.println("Too many agents try to resolve the sudoku");
	}
	
	public Structure getTypeOfConnectionFromAnalyseId (int id){return connectionArray.get(id).getType();}
	public int getIndexOfConnectionFromAnalyseId (int id) {return connectionArray.get(id).getIndex();}
	
	
	//Members
	private Case m_sudoku[][];
	private PropertyChangeSupport m_pcs;
	private HashMap<AID, EnvironnementAnalyseCorresp> connectionArray;
	
	
	
	static class EnvironnementAnalyseCorresp {
		private Integer m_index;
		private Structure m_type;
		private AID m_analyseId;
		static private int numberOfEntries = 0;
		
		public EnvironnementAnalyseCorresp(int index, Structure type, AID analyseId) {
			m_index = index;
			m_type = type;
			m_analyseId = analyseId;
			numberOfEntries++;
		}

		public Integer getIndex() {
			return m_index;
		}
		
		static public int getNumberOfEntries() {return numberOfEntries;}

		public void setIndex(Integer m_index) {
			this.m_index = m_index;
		}

		public Structure getType() {
			return m_type;
		}

		public void setType(Structure m_type) {
			this.m_type = m_type;
		}

		public AID getAnalyseId() {
			return m_analyseId;
		}

		public void setAnalyseId(AID m_analyseId) {
			this.m_analyseId = m_analyseId;
		}
	}
	
	
	
}
