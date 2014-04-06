package sudoku_solver;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ReturnProcessResult extends OneShotBehaviour {

	public ReturnProcessResult(AnaAgent a) {
		super(a);
		m_myAgent = a;
	}

	@Override
	public void action() {
		ACLMessage response = new ACLMessage(ACLMessage.INFORM);
		response.addReceiver(m_myAgent.get_m_simulAgent());
		ArrayList<Case> result = m_myAgent.get_m_array();
		
		ObjectMapper writerMapper = new ObjectMapper();
		try{
			HashMap<String, ArrayList<Case>> casesMap = new HashMap<String, ArrayList<Case>>();
			casesMap.put("cases", result);
			HashMap<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("content", casesMap);
			StringWriter sw = new StringWriter();
			writerMapper.writeValue(sw, contentMap);
			response.setContent(sw.toString());
			myAgent.send(response);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Members
	private AnaAgent m_myAgent;

}
