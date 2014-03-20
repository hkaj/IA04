package factorielleSMA;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class FactReceiveMessageBehaviour extends CyclicBehaviour {

	public FactReceiveMessageBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage order = myAgent.receive();
		
		if (order != null)
		{
			if (order.getPerformative() == ACLMessage.REQUEST)
			{
				String sender = order.getSender().toString();
				System.out.println(sender);
				if (order.getSender().toString().equals("( agent-identifier :name console )"))
				{
					myAgent.addBehaviour(new FactBehaviourConsoleTrigger(myAgent, order));
				}
			}
			else if (order.getPerformative() == ACLMessage.INFORM)
			{
				myAgent.addBehaviour(new FactBehaviour(myAgent, order));
			}
			else if (order.getPerformative() == ACLMessage.FAILURE)
			{
				System.out.println("Failure message from " + order.getSender().getName() + " : " + order.getContent());
			}
			
		}

	}

}
