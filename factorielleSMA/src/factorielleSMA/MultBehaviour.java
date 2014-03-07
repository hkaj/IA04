package factorielleSMA;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MultBehaviour extends CyclicBehaviour {
	
	public MultBehaviour(Agent a) {
		super(a);
	}
	
	private void sendResult(String res) {
		ACLMessage response = new ACLMessage(ACLMessage.INFORM);
		response.addReceiver( new AID("factAgent", AID.ISLOCALNAME));
		response.setContent(res);
		myAgent.send(response);
	}

	public void action() {
			String result;
			ACLMessage order = myAgent.blockingReceive();
			if (order != null) {
				System.out.println(
					myAgent.getName() + " says : I received this -> \n" + order + "\nContent :\n" + order.getContent()
				);
				String[] ops = order.getContent().toString().split(" ");
				int op1 = Integer.parseInt(ops[0]);
				int op2 = Integer.parseInt(ops[1]);
				result = Integer.toString(op1 * op2);
				this.sendResult(result);
			}
	}
	
}
