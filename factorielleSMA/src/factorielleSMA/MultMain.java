package factorielleSMA;
import jade.core.ProfileImpl;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MultMain {
	
	public static String MAIN_PROPERTIES_FILE = "src/mult_properties";	
	public static void main(String[] args) throws StaleProxyException  {

		Runtime rt = Runtime.instance();
		try{
			Profile pMult = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController multContainer = rt.createMainContainer(pMult);
			AgentController multAgent = multContainer.createNewAgent("multAgent", MultAgent.class.getName(), new Object[0]);
			multAgent.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}
}
