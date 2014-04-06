package sudoku_solver;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SudokuMain {
	
	public static String MAIN_PROPERTIES_FILE = "src/mainProperties";
	public static String SECOND_PROPERTIES_FILE = "src/secondProperties";

	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		
		//Création agent Environnement
		try {
			Profile p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController envContainer = rt.createMainContainer(p);
			AgentController envAgent = envContainer.createNewAgent("EnvAgent", EnvAgent.class.getName(), new Object[0]);
			envAgent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Création agent Simulation
		try {
			Profile p = new ProfileImpl(SECOND_PROPERTIES_FILE);
			ContainerController simulContainer = rt.createAgentContainer(p);
			AgentController simulAgent = simulContainer.createNewAgent("SimAgent", EnvAgent.class.getName(), new Object[0]);
			simulAgent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Création agents d'Analyse
		try {
			Profile p = new ProfileImpl(SECOND_PROPERTIES_FILE);
			ContainerController anaContainer = rt.createAgentContainer(p);
			for (int i = 0; i < 27; i++){
				AgentController anaAgent = anaContainer.createNewAgent("AnaAgent" + Integer.toString(i), EnvAgent.class.getName(), new Object[0]);
				anaAgent.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
