package ontologie;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class OntologyMain {

	static public String MAIN_PROPERTIES_FILE = "src/mainProperties";
	static public String SECOND_PROPERTIES_FILE = "src/secondProperties";
	
	public static void main(String[] args) throws StaleProxyException  {

		Runtime rt = Runtime.instance();
		
		//Instanciation de l'agent d'interface
		try{
			Profile p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController intContainer = rt.createMainContainer(p);
			AgentController interfaceAgent = intContainer.createNewAgent("InterfaceAgent", InterfaceAgent.class.getName(), new Object[0]);

			interfaceAgent.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		//Instanciation de l'agent chargé de la base de connaissances
		try{
			Profile p = new ProfileImpl(SECOND_PROPERTIES_FILE);
			ContainerController kbContainer = rt.createMainContainer(p);
			AgentController kbAgent = kbContainer.createNewAgent("KB", KnowledgeBaseAgent.class.getName(), new Object[0]);

			kbAgent.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
