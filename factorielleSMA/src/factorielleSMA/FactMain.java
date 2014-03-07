package factorielleSMA;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class FactMain {
	
	public static String MAIN_PROPERTIES_FILE = "src/fact_properties";
	public static void main(String[] args) throws StaleProxyException  {

		Runtime rt = Runtime.instance();
		try{
			Profile p = new ProfileImpl("127.0.0.1", 1338, "tdia04", false);
			ContainerController factContainer = rt.createAgentContainer(p);
			AgentController factAgent = factContainer.createNewAgent("factAgent", FactAgent.class.getName(), new Object[0]);
			factAgent.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
