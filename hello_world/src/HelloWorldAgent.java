import jade.core.Agent;

// Agent du conteneur secondaire effectuant le traitement
@SuppressWarnings("serial")
public class HelloWorldAgent extends Agent {
	// m�thode orchestrant le comportement g�n�ral de l'agent
	protected void setup() {
		System.out.println("Hello World");
		System.out.println(this.getName());
		Object[] args = getArguments();
		String op = (String) args[0];
		// ajout de la behaviour � l'agent
		addBehaviour(new HelloBehaviour(this,op));
	}
}