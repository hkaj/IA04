import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

// Classe utilisatrice/isntanciatrice du container secondaire
public class HelloMain {
	public static void main(String[] args) throws StaleProxyException  {
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl("127.0.0.1", 1337, "tdia04", false);
		Object[] arg = new Object[1];
		arg[0] = "toto";
		try{
			// cr�e le conteneur et l'agent dedans
			ContainerController cc = rt.createAgentContainer(p);
			AgentController ac = cc.createNewAgent("Hello",
			"HelloWorldAgent", arg);
			
			// call the setup method of our agent.
			ac.start();
		}
		catch(Exception ex){
			System.out.println("exception HElloMain");
	}
	}
}
