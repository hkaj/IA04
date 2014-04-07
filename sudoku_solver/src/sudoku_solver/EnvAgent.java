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

import view.SudokuWindow;

public class EnvAgent extends Agent {
	
	//Enumération pour définir le type de zone du sudoku
	public static enum Zone {LINE, COLUMN, SQUARE};
	
	public EnvAgent() throws FileNotFoundException, IOException {
		super();
		m_sudoku = new Case[9][9];
		connectionArray = new HashMap<AID, EnvironnementAnalyseCorresp>();
	}

	@Override
	protected void setup() {
		
		m_pcs = new PropertyChangeSupport(this);
		
		//Get the arguments of Agent
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
	        }
	        finally {
	        	br.close();
	        }
	    } catch (IOException e1) {
			e1.printStackTrace();
	    }
		
	    
	    //Create the window
	    m_sudokuWindow = new SudokuWindow(this);
		
	    //On lance le Behaviour de lancement de requête aux agents Analyse
		addBehaviour(new EnvReceiveMessageBehaviour(this));		
	}
	
	
	
	
	

	//Events for GUI
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
	public int getCaseValue(int lig, int col) {
		if (lig < 9 && col < 9)
			return m_sudoku[lig][col].getValue();
		else
			return -1;
	}
	
	
	
	
	
	//Gestion de la table de correspondance entre AgentAnalyse et ligne, colonne et carré
	public void setConnectionAnalyseStructure (AID analyseId) {
		int numberOfEntries = EnvironnementAnalyseCorresp.getNumberOfEntries();
		if(numberOfEntries < 27){
			Zone type;
			if (numberOfEntries < 9) //from 0 to 8 -> LINE
				type = Zone.LINE;
			else if (numberOfEntries < 18) //from 9 to 17 -> COLUMN
				type = Zone.COLUMN;
			else //from 18 to 26 -> SQUARE
				type = Zone.SQUARE;
			connectionArray.put(analyseId, new EnvironnementAnalyseCorresp(numberOfEntries%9, type, analyseId));
		}
		else
			System.out.println("Too many agents try to resolve the sudoku");
	}
	
	//Accès à la table de correspondance en fonction d'un AID d'agent
	public Zone getTypeOfConnectionFromAnalyseId (AID id){return connectionArray.get(id).getType();}
	public int getIndexOfConnectionFromAnalyseId (AID id) {return connectionArray.get(id).getIndex();}
	public AID getRealAIDofConnectionFromAnalyseId (AID id) {return connectionArray.get(id).getAnalyseId();}
	
	public ArrayList<Case> getListOfCasesFromAID(AID id){
		/**
		 * @brief Construit la liste des cases pour un agent d'analyse donné
		 */
		
		
		//L'index est le numéro de ligne, colonne ou carré
		//Le type peut être ligne, colonne ou carré
		Zone type = connectionArray.get(id).getType();
		int index = connectionArray.get(id).getIndex();
		ArrayList<Case> newList = new ArrayList<>(9);
		
		if (type == EnvAgent.Zone.LINE){
			//La zone est une ligne
			for(int i = 0; i < 9; i++){
				newList.add(m_sudoku[index][i]);
			}
			
		} else if (type == EnvAgent.Zone.COLUMN){
			//La zone est une colonne
			for(int i = 0; i < 9; i++){
				newList.add(m_sudoku[i][index]);
			}
			
		} else{
			//La zone est un carré
			
			//On prend pour point de départ la case en haut à gauche du carré
			//Ainsi pour le carré numéro 3 (centre gauche du sudoku) :
			//i = (3 / 3) * 3 = 1 * 3 = 3
			//j = (3 % 3) * 3 = 0 * 3 = 0
			int starti = (index / 3) * 3, i = starti;
			int startj = (index % 3) * 3, j = startj;
			
			while(i != starti + 3){
				newList.add(m_sudoku[i][j]);
				
				/*if (index == 3)
					System.out.println("i = " + i + "j = " + j);*/
				
				if ((j+1) % 3 == 0){
					//Si on a visité trois cases on passe à la colonne suivante
					i++; j = startj;
				}
				else
					//Sinon on passe à la ligne suivante pour une même colonne
					j++;
			}
			
		}
		
		return newList;
	}
	
	//Une zone est résolue si l'ensemble de ses cases a unevaleur != 0
	public boolean isZoneResolved(AID id){
		EnvironnementAnalyseCorresp corresp = connectionArray.get(id);
		int index = corresp.getIndex();
		Zone type = corresp.getType();
		
		if (type == EnvAgent.Zone.LINE){
			
			for(int i = 0; i < 9; i++){
				if (m_sudoku[index][i].getValue() == 0)
					return false;
			}
		} else if (type == EnvAgent.Zone.COLUMN){
			for(int i = 0; i < 9; i++){
				if (m_sudoku[i][index].getValue() == 0)
					return false;
			}
			
		} else{
			//Meme principe que la méthode précédente
			int starti = (index / 3) * 3, i = starti;
			int startj = (index % 3) * 3, j = startj;
			while(i != starti + 3){
				if (m_sudoku[i][j].getValue() == 0)
					return false;
				
				if ((j+1) % 3 == 0){
					i++; j = startj;
				}
				else
					j++;
			}
		}
		
		return true;
	}
	
	
	//Le sudoku est résolue dès que toutes les cases ont une valeur != 0
	public boolean isSudokuSolved() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (m_sudoku[i][j].getValue() == 0)
					return false;
		return true;
	}
	
	
	//Members
	private Case m_sudoku[][];	//LE sudoku
	private HashMap<AID, EnvironnementAnalyseCorresp> connectionArray;	//La table de correspondances
	private SudokuWindow m_sudokuWindow;	//La vue
	private PropertyChangeSupport m_pcs;	//Le Property Change Support
	

	
	//Classe pour la gestion de chaque correspondance
	static class EnvironnementAnalyseCorresp {
		private Integer m_index;	//Le numéro de ligne, colonne, carré
		private Zone m_type;	//Le type de zone (ligne colonne ou carré)
		private AID m_analyseId;	//L'AID de l'agent
		static private int numberOfEntries = 0;	//Le nombre d'agents enregistrés
		
		public EnvironnementAnalyseCorresp(int index, Zone type, AID analyseId) {
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

		public Zone getType() {
			return m_type;
		}

		public void setType(Zone m_type) {
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
