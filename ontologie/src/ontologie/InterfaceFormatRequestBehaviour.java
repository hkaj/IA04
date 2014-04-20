package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InterfaceFormatRequestBehaviour extends OneShotBehaviour {

	public InterfaceFormatRequestBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
	}

	@Override
	public void action() {
		String[] data;
		data = m_message.getContent().split("|");
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		ObjectMapper writerMapper = new ObjectMapper();
		try{
			// TODO: BUILD REQUEST HERE
			// ...
			StringWriter sw = new StringWriter();
			// TODO: Fill sw
			request.setContent(sw.toString());
			myAgent.send(request);
		}catch (Exception e){
			e.printStackTrace();
		}
		if (data.length == 3) {
			if (data[0] != "") {
				
			}
		} else {
			System.out.println("The request sent through the console isn't correctly formatted.");
		}

	}
	
	//Members
	private ACLMessage m_message;

}
