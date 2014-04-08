package sudoku_solver;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvSendRequestToAnalBehaviour extends OneShotBehaviour {

	public EnvSendRequestToAnalBehaviour(Agent a, AID receiver, AID simul) {
		super(a);
		m_receiver = receiver;
		m_simul = simul;
	}

	@Override
	public void action() {
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(m_receiver);
		
		//On vérifie d'abord que le sudoku n'est pas déjà résolu
		EnvAgent agent = (EnvAgent) myAgent;
		if (!agent.isSudokuSolved()){
			//On récupère les listes de case pour un agent d'Analyse donné
			ArrayList<Case> zoneCases = agent.getListOfCasesFromAID(m_receiver);
			
			//DEBUG
			/*if (agent.getIndexOfConnectionFromAnalyseId(m_receiver) == 5 && agent.getTypeOfConnectionFromAnalyseId(m_receiver) == EnvAgent.Zone.LINE){
				System.out.println("TO BE SENT");
				for (Iterator<Case> it = zoneCases.iterator(); it.hasNext();){
					Case nex = it.next();
					System.out.println("Value : " + nex.getValue());
					for (Iterator<Integer> it2 = nex.getPossibilities().iterator(); it2.hasNext();){
						System.out.println(it2.next());
					}
				}
			}*/
			//DEBUG
			
			//Parsing de la requête
			ObjectMapper writerMapper = new ObjectMapper();
			try{
				HashMap<String, ArrayList<Case>> casesMap = new HashMap<String, ArrayList<Case>>();
				casesMap.put("cases", zoneCases);
				HashMap<String, Object> contentMap = new HashMap<String, Object>();
				contentMap.put("content", casesMap);
				StringWriter sw = new StringWriter();
				writerMapper.writeValue(sw, contentMap);
				order.setContent(sw.toString());
			}catch (Exception e){
				e.printStackTrace();
			}
			
			//System.out.println(order.getContent());
			
			myAgent.send(order);
			//System.out.println("Message sent");
			//System.out.println(m_receiver);
			
		} else {
			//On envoie un message à Simul pour lui dire que le Sudoku est résolu
			ACLMessage sudokuSolvedMessage = new ACLMessage(ACLMessage.CONFIRM);
			sudokuSolvedMessage.addReceiver(m_simul);
			myAgent.send(sudokuSolvedMessage);
		}

	}
	
	//Members
	private AID m_receiver;
	private AID m_simul;

}
