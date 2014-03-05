import jade.core.behaviours.Behaviour;
import jade.core.Agent;

// Behaviour de HelloAgent
public class HelloBehaviour extends Behaviour {
	private String op;
	
	// Accesseur
	public void set_op(String op) {
		this.op = op;
	}
	// Constructeur surchargé pour prend l'opérateur en arg
	public HelloBehaviour(Agent a, String op) {
		super(a);
		set_op(op);
	}
	// Action concrète de la behaviour
	public void action() {
		System.out.println("Contact" + op);
	}
	// renseigne sur le succès du traitement
	public boolean done() {
		return true;
	}
}
