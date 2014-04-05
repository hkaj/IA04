package sudoku_solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvUpdateSudokuBehaviour extends OneShotBehaviour {

	public EnvUpdateSudokuBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_sudokuNewValuesMessage = message;
	}

	@Override
	public void action() {
		String content = m_sudokuNewValuesMessage.getContent();
		ObjectMapper mapper = new ObjectMapper();
		
		
		try {
			JsonNode jrootNode = mapper.readValue(content, JsonNode.class);
			HashMap<AID,ArrayList<Case>> ensembleCases = mapper.readValue(jrootNode.path("content").textValue(), new TypeReference<HashMap<AID,ArrayList<Case>>>(){});
			for(Iterator<Entry<AID,ArrayList<Case>>> it = ensembleCases.entrySet().iterator(); it.hasNext();){
				Entry<AID, ArrayList<Case>> pairs = (Entry<AID, ArrayList<Case>>)it.next();
		        processAIDandCases((AID) pairs.getKey(), (ArrayList<Case>) pairs.getValue());
		        myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent, (AID) pairs.getKey()));
		    }
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	private void processAIDandCases(AID id, ArrayList<Case> value) {
		
		EnvAgent agent = (EnvAgent) myAgent;
		EnvAgent.Structure type = agent.getTypeOfConnectionFromAnalyseId(id);
		int index = agent.getIndexOfConnectionFromAnalyseId(id);
		Case[][] sudoku = agent.getSudoku();
		
		if (type == EnvAgent.Structure.LINE){
			int i = 0;
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				sudoku[index][i].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[index][i].getPossibilities(), it.next().getPossibilities()));
				
				if (sudoku[index][i].getPossibilities().size() == 1){
					sudoku[index][i].setValue(sudoku[index][i].getPossibilities().get(0));
					sudoku[index][i].getPossibilities().clear();
				}
				
			}
		} else if (type == EnvAgent.Structure.COLUMN){
			int i = 0;
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				sudoku[i][index].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][index].getPossibilities(), it.next().getPossibilities()));
			
			
				if (sudoku[i][index].getPossibilities().size() == 1){
					sudoku[i][index].setValue(sudoku[i][index].getPossibilities().get(0));
					sudoku[i][index].getPossibilities().clear();
				}
			}
			
		} else{
			int starti = (index % 3) * 3, i = starti;
			int j = index;
			for(Iterator<Case> it = value.iterator(); it.hasNext();){
				sudoku[i][j].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][j].getPossibilities(), it.next().getPossibilities()));
				
				if (sudoku[i][j].getPossibilities().size() == 1){
					sudoku[i][j].setValue(sudoku[i][j].getPossibilities().get(0));
					sudoku[i][j].getPossibilities().clear();
				}
				
				if ((i+1) % 3 == 0){
					j++; i = starti;
				}
				else
					i++;
			}
		}
		
		//On notifie la vue
		agent.firePropertyChange("Sudoku_changed", null, sudoku);
		
		
	}
	
	
	private ArrayList<Integer> intersectionBetweenCasePossibilityList (ArrayList<Integer> first, ArrayList<Integer> second){
		ArrayList<Integer> newList = new ArrayList<Integer>();
		
		for (Iterator<Integer> it = first.iterator(); it.hasNext();){
			Integer current = it.next();
			if (second.contains(current)){
				newList.add(current);
			}
		}
		
		return newList;
	}

	//Members
	ACLMessage m_sudokuNewValuesMessage;

}
