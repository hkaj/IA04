import jade.core.ProfileImpl;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

// Instancie et utilise le conteneur principal
public class mainBoot {
	public static String MAIN_PROPERTIES_FILE = "src/properties";
	public static void main(String[] args) throws StaleProxyException  {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
