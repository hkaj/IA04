import jade.core.behaviours.Behaviour;
import jade.core.Agent;

// Behaviour de HelloAgent
@SuppressWarnings("serial")
public class HelloBehaviour extends Behaviour {
	private String op;
	
	// Accesseur
	public void set_op(String op) {
		this.op = op;
	}
	// Constructeur surcharg� pour prend l'op�rateur en arg
	public HelloBehaviour(Agent a, String op) {
		super(a);
		set_op(op);
	}
	// Action concr�te de la behaviour
	public void action() {
		System.out.println("Contact" + op);
	}
	// renseigne sur le succ�s du traitement
	public boolean done() {
		return true;
	}
}
