package sudoku_solver;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			String json = jrootNode.path("content").path("cases").toString();
			Map<String, ArrayList<Case>> result = new HashMap<String, ArrayList<Case>>();
			result = mapper.readValue(json, new TypeReference<Map<String, ArrayList<Case>>>(){});
			for (String name : result.keySet()) {
				AID aid = new AID(name, AID.ISGUID);
				ArrayList<Case> array = result.get(aid.getName());
				System.out.println(aid.getLocalName() + "'s array length = " + array.size());
				AID realId = processAIDandCases(aid, array);
				myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent, realId, m_sudokuNewValuesMessage.getSender()));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private AID processAIDandCases(AID id, ArrayList<Case> value) {
		
		EnvAgent agent = (EnvAgent) myAgent;
		EnvAgent.Structure type = agent.getTypeOfConnectionFromAnalyseId(id);
		AID returnAID = agent.getRealAIDofConnectionFromAnalyseId(id);
		int index = agent.getIndexOfConnectionFromAnalyseId(id);
		Case[][] sudoku = agent.getSudoku();
		
		if (type == EnvAgent.Structure.LINE){
			//La zone de l'agent était une ligne
			int i = 0;
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				sudoku[index][i].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[index][i].getPossibilities(), it.next().getPossibilities()));
				
				if (sudoku[index][i].getPossibilities().size() == 1){
					sudoku[index][i].setValue(sudoku[index][i].getPossibilities().get(0));
					sudoku[index][i].getPossibilities().clear();
					agent.firePropertyChange("Case_changed", null, new int[] {index, i, sudoku[index][i].getValue()});
				}
				
			}
		} else if (type == EnvAgent.Structure.COLUMN){
			//La zone de l'agent était une colonne
			int i = 0;
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				sudoku[i][index].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][index].getPossibilities(), it.next().getPossibilities()));
			
			
				if (sudoku[i][index].getPossibilities().size() == 1){
					sudoku[i][index].setValue(sudoku[i][index].getPossibilities().get(0));
					sudoku[i][index].getPossibilities().clear();
					agent.firePropertyChange("Case_changed", null, new int[] {i, index, sudoku[i][index].getValue()});
				}
			}
			
		} else{
			//La zone de l'agent était un carré
			int starti = (index % 3) * 3, i = starti;
			int j = (index / 3) * 3;
			for(Iterator<Case> it = value.iterator(); it.hasNext();){
				sudoku[i][j].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][j].getPossibilities(), it.next().getPossibilities()));
				
				if (sudoku[i][j].getPossibilities().size() == 1){
					sudoku[i][j].setValue(sudoku[i][j].getPossibilities().get(0));
					sudoku[i][j].getPossibilities().clear();
					agent.firePropertyChange("Case_changed", null, new int[] {i, j, sudoku[i][j].getValue()});
				}
				
				if ((i+1) % 3 == 0){
					j++; i = starti;
				}
				else
					i++;
			}
		}
		
		return returnAID;
		
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
