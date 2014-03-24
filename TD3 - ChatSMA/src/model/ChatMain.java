package model;

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
			AgentController chatAgent2 = chatAgentContainer.createNewAgent("usr2", ChatAgent.class.getName(), new Object[0]);
			chatAgent.start();
			chatAgent2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
