package model;

import mult.MultAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ChatMain {
	
	public static String MAIN_PROPERTIES_FILE = "src/mainProperties";
	public static String SECOND_PROPERTIES_FILE = "src/secondProperties";
	
	public static void main(String[] args) throws StaleProxyException {
		Runtime rt = Runtime.instance();
		
		try {
			Profile p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController chatAgentContainer = rt.createMainContainer(p);
			AgentController chatAgent = chatAgentContainer.createNewAgent("usr1", ChatAgent.class.getName(), new Object[0]);
			chatAgent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			Profile p2 = new ProfileImpl(SECOND_PROPERTIES_FILE);
//			ContainerController chatSecondaryContainer = rt.createAgentContainer(p2);
//			AgentController chatSecondAgent = chatSecondaryContainer.createNewAgent("usr2", ChatAgent.class.getName(), new Object[0]);
//			chatSecondAgent.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try{
			Profile pMult = new ProfileImpl(SECOND_PROPERTIES_FILE);
			ContainerController multContainer = rt.createAgentContainer(pMult);
			
			//Création de trois agents multiplicateurs
			AgentController multAgent = multContainer.createNewAgent("multAgent1", MultAgent.class.getName(), new Object[0]);
			AgentController multAgent2 = multContainer.createNewAgent("multAgent2", MultAgent.class.getName(), new Object[0]);
			AgentController multAgent3 = multContainer.createNewAgent("multAgent3", MultAgent.class.getName(), new Object[0]);
			multAgent.start();
			multAgent2.start();
			multAgent3.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

}
