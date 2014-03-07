package factorielleSMA;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class FactBehaviour extends Behaviour {
	
	public FactBehaviour(Agent a) {
		super(a);
	}

	public void action() {
		int nb = 6;
		int tmp = nb;
		int res = 1;
		ACLMessage response;
		while (tmp > 1) {
			this.sendOrder(tmp, tmp-1);
			response = myAgent.blockingReceive();
			if (response != null) {
				res *= Integer.parseInt(response.getContent());
				tmp -= 2;
			}
		}
		// printing the result
		System.out.println(nb + "! = " + res);
	}
	
	private void sendOrder(int nb1, int nb2) {
		// forging the content
		String message = nb1 + " " + nb2;
		// instantiating the message (request type)
		ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
		order.addReceiver(new AID("multAgent", AID.ISLOCALNAME)); // we can use myAgent.multAgent
		order.setContent(message);
		// sending the calculus request
		myAgent.send(order);
	}
	
	public boolean done() {
		return true;
	}

}
