package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EnvAgent extends Agent {
	
	public static enum Structure {LINE, COLUMN, SQUARE};
	
	public EnvAgent() throws FileNotFoundException, IOException {
		super();
		m_sudoku = new Case[9][9];
	}

	@Override
	protected void setup() {
		
		m_pcs = new PropertyChangeSupport(this);
		
		//Get the arguments
		String filename = (String) getArguments()[0];
		
		//Parser le fichier contenant le sudoku
		BufferedReader br;
		
	    try {
	    	br = new BufferedReader(new FileReader(filename));
	        String line = br.readLine();
	        int i = 0, j = 0;
	        
	        try{
	        	while (line != null && i < 9) {
	        		
		        	for (j = 0; j < 9; j++){
		        		int value = Character.getNumericValue(line.charAt(j));
		        		m_sudoku[i][j] = new Case(value);
		        	}
		   
		            line = br.readLine();
		            ++i;
	        	}
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("Iteration i = " + i + " et j = " + j);
	        }
	        finally {
	        	br.close();
	        }
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	    }
		
		
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
	@SuppressWarnings("unused")
	private void setSudoku(Case[][] m_sudoku) {this.m_sudoku = m_sudoku;}
	
	
	
	
	
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
	
	public Structure getTypeOfConnectionFromAnalyseId (AID id){return connectionArray.get(id).getType();}
	public int getIndexOfConnectionFromAnalyseId (AID id) {return connectionArray.get(id).getIndex();}
	
	public ArrayList<Case> getListOfCasesFromAID(AID id){
		Structure type = connectionArray.get(id).getType();
		int index = connectionArray.get(id).getIndex();
		ArrayList<Case> newList = new ArrayList<>(9);
		
		if (type == EnvAgent.Structure.LINE){
			
			for(int i = 0; i < 9; i++){
				newList.add(m_sudoku[index][i]);
			}
			
		} else if (type == EnvAgent.Structure.COLUMN){
			
			for(int i = 0; i < 9; i++){
				newList.add(m_sudoku[i][index]);
			}
			
		} else{
			
			int starti = (index % 3) * 3, i = starti;
			int startj = index, j = startj;
			
			while(j != startj + 3){
				newList.add(m_sudoku[i][j]);
				
				if ((i+1) % 3 == 0){
					j++; i = starti;
				}
				else
					i++;
			}
			
		}
		
		return newList;
	}
	
	public boolean isZoneResolved(AID id){
		EnvironnementAnalyseCorresp corresp = connectionArray.get(id);
		int index = corresp.getIndex();
		Structure type = corresp.getType();
		
		if (type == EnvAgent.Structure.LINE){
			
			for(int i = 0; i < 9; i++){
				if (m_sudoku[index][i].getValue() == 0)
					return false;
			}
		} else if (type == EnvAgent.Structure.COLUMN){
			for(int i = 0; i < 9; i++){
				if (m_sudoku[i][index].getValue() == 0)
					return false;
			}
			
		} else{
			int starti = (index % 3) * 3, i = starti;
			int startj = index, j = startj;
			while(j != startj + 3){
				if (m_sudoku[i][j].getValue() == 0)
					return false;
				
				if ((i+1) % 3 == 0){
					j++; i = starti;
				}
				else
					i++;
			}
		}
		
		return true;
	}
	
	
	public boolean isSudokuSolved() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (m_sudoku[i][j].getValue() == 0)
					return false;
		return true;
	}
	
	
	//Members
	private Case m_sudoku[][];
	private HashMap<AID, EnvironnementAnalyseCorresp> connectionArray;
	
	private PropertyChangeSupport m_pcs;
	
	
	
	
	
	
	
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
