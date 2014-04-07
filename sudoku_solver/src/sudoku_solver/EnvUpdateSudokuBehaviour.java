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
			
			//Pour chaque Agent d'Analyse
			for (String name : result.keySet()) {
				AID aid = new AID(name, AID.ISGUID);
				ArrayList<Case> array = result.get(aid.getName());
				
				//DEBUG
				/*EnvAgent agent = (EnvAgent) myAgent;
				if (agent.getIndexOfConnectionFromAnalyseId(aid) == 5 && agent.getTypeOfConnectionFromAnalyseId(aid) == EnvAgent.Structure.LINE){
					for (Iterator<Case> it = array.iterator(); it.hasNext();){
						Case nex = it.next();
						System.out.println("Value : " + nex.getValue());
						for (Iterator<Integer> it2 = nex.getPossibilities().iterator(); it2.hasNext();){
							System.out.println(it2.next());
						}
					}
				}
				System.out.println(aid.getLocalName() + "'s array length = " + array.size());*/
				//DEBUG
				
				AID realId = processAIDandCases(aid, array);
				
				//On envoie une nouvelle requête à l'Analyse Agent
				myAgent.addBehaviour(new EnvSendRequestToAnalBehaviour(myAgent, realId, m_sudokuNewValuesMessage.getSender()));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private AID processAIDandCases(AID id, ArrayList<Case> value) {
		
		EnvAgent agent = (EnvAgent) myAgent;
		EnvAgent.Zone type = agent.getTypeOfConnectionFromAnalyseId(id);
		AID returnAID = agent.getRealAIDofConnectionFromAnalyseId(id);
		int index = agent.getIndexOfConnectionFromAnalyseId(id);
		Case[][] sudoku = agent.getSudoku();
		
		if (type == EnvAgent.Zone.LINE){
			//La zone de l'agent était une ligne
			int i = 0;
			
			//Pour chaque case
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				Case current = it.next();
				
				//On met à jour la liste des possibilités
				sudoku[index][i].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[index][i].getPossibilities(), current.getPossibilities()));
				
				//Si la case a été découverte on la met aussi à jour
				if (current.getValue() != 0 && sudoku[index][i].getValue() == 0)
					sudoku[index][i].setValue(current.getValue());
				
				//Si la liste des possibilités est réduite à un seul élément, on a trouvé la valeur
				if (sudoku[index][i].getPossibilities().size() == 1){
					sudoku[index][i].setValue(sudoku[index][i].getPossibilities().get(0));
					sudoku[index][i].getPossibilities().clear();
				}
				
				//On notifie la vue
				agent.firePropertyChange("Case_changed", null, new int[] {index, i, sudoku[index][i].getValue()});
				
			}
		} else if (type == EnvAgent.Zone.COLUMN){
			//La zone de l'agent était une colonne
			int i = 0;
			
			//Pour chaque case
			for(Iterator<Case> it = value.iterator(); it.hasNext(); i++){
				
				Case current = it.next();
				
				//On met à jour la liste des possibilités
				sudoku[i][index].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][index].getPossibilities(), current.getPossibilities()));
				
				//Si la case a été découverte on la met aussi à jour
				if (current.getValue() != 0 && sudoku[i][index].getValue() == 0)
					sudoku[i][index].setValue(current.getValue());
			
				//Si la liste des possibilités est réduite à un seul élément, on a trouvé la valeur
				if (sudoku[i][index].getPossibilities().size() == 1){
					sudoku[i][index].setValue(sudoku[i][index].getPossibilities().get(0));
					sudoku[i][index].getPossibilities().clear();
				}
				
				//On notifie la vue
				agent.firePropertyChange("Case_changed", null, new int[] {i, index, sudoku[i][index].getValue()});
			}
			
		} else{
			//La zone de l'agent était un carré
			
			//On prend pour point de départ la case en haut à gauche du carré
			//Ainsi pour le carré numéro 3 (centre gauche du sudoku) :
			//i = (3 / 3) * 3 = 1 * 3 = 3
			//j = (3 % 3) * 3 = 0 * 3 = 0
			int starti = (index / 3) * 3, i = starti;
			int startj = (index % 3) * 3, j = startj;
			
			//Pour chaque case
			for(Iterator<Case> it = value.iterator(); it.hasNext();){
				Case current = it.next();
				
				//On met à jour la liste des possibilités
				sudoku[i][j].setPossibilities(intersectionBetweenCasePossibilityList(sudoku[i][j].getPossibilities(), current.getPossibilities()));
				
				//Si la case a été découverte on la met aussi à jour
				if (current.getValue() != 0 && sudoku[i][j].getValue() == 0)
					sudoku[i][j].setValue(current.getValue());
				
				//Si la liste des possibilités est réduite à un seul élément, on a trouvé la valeur
				if (sudoku[i][j].getPossibilities().size() == 1){
					sudoku[i][j].setValue(sudoku[i][j].getPossibilities().get(0));
					sudoku[i][j].getPossibilities().clear();
				}
				
				//On notifie la vue
				agent.firePropertyChange("Case_changed", null, new int[] {i, j, sudoku[i][j].getValue()});
				
				/*if (index == 3)
					System.out.println("i = " + i + "j = " + j);*/
				
				if ((j+1) % 3 == 0){
					i++; j = startj;
				}
				else
					j++;
			}
		}
		
		return returnAID;
		
	}
	
	
	private ArrayList<Integer> intersectionBetweenCasePossibilityList (ArrayList<Integer> first, ArrayList<Integer> second){
		
		/** 
		 * @brief Calcule l'intersection entre deux listes
		 */
		
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
