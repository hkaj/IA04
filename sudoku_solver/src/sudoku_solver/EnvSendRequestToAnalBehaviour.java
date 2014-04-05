package sudoku_solver;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EnvSendRequestToAnalBehaviour extends OneShotBehaviour {

	public EnvSendRequestToAnalBehaviour(Agent a, AID receiver) {
		super(a);
		m_receiver = receiver;
	}

	@Override
	public void action() {
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(m_receiver);
		
		//On vérifie d'abord que la zone dont doit s'occuper l'AnaAgent n'est pas déjà résolue
		EnvAgent agent = (EnvAgent) myAgent;
		if (!agent.isZoneResolved(m_receiver)){
			ArrayList<Case> zoneCases = agent.getListOfCasesFromAID(m_receiver);
			
			
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
			
			
			
			myAgent.send(order);
		}else{
			System.out.println("Zone résolue, pas de message envoyé");
		}

	}
	
	//Members
	private AID m_receiver;

}
