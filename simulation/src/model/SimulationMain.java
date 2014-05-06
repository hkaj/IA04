package model;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SimulationMain {

	public static final String MAIN_PROPERTIES_FILE = "src/simul_properties";
	public static final String BUG_PROPERTIES_FILE = "src/bug_properties";
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		try{
			Profile pMult = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController multContainer = rt.createMainContainer(pMult);
			
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
