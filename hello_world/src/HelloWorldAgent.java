import jade.core.Agent;

// Agent du conteneur secondaire effectuant le traitement
public class HelloWorldAgent extends Agent {
	// méthode orchestrant le comportement général de l'agent
	protected void setup() {
		System.out.println("Hello World");
		System.out.println(this.getName());
		Object[] args = getArguments();
		String op = (String) args[0];
		// ajout de la behaviour à l'agent
		addBehaviour(new HelloBehaviour(this,op));
	}
}