package sudoku_solver;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class sendResultsBehaviour extends OneShotBehaviour {

	public sendResultsBehaviour(SimulAgent a) {
		super(a);
		m_myAgent = a;
	}
	
	public void action() {
		System.out.println("All the AnaAgents returned. Sending the result to EnvAgent.");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(m_myAgent.get_m_envAgent());
		
		ObjectMapper writerMapper = new ObjectMapper();
		try{
			HashMap<String, Map<String, ArrayList<Case>>> casesMap = new HashMap<String, Map<String, ArrayList<Case>>>();
			casesMap.put("cases", m_myAgent.get_m_result());
			HashMap<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", casesMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			message.setContent(sw.toString());
			myAgent.send(message);
		}catch (Exception e){
			e.printStackTrace();
		}
		m_myAgent.clear_m_result();
	}
	
	// Members
	private SimulAgent m_myAgent; 

}
