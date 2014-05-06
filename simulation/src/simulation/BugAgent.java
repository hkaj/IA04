package simulation;

import jade.core.Agent;

public class BugAgent extends Agent {

	public BugAgent() {
		super();
	}
	
	@Override
	protected void setup() {
		//Initializing the insect capacities
		DISTANCE_DEPLACEMENT = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_DEPLACEMENT());
		DISTANCE_PERCEPTION = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_PERCEPTION());
		CHARGE_MAX = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_CHARGE());
		
		//Add Behaviours
		addBehaviour(new BugReceiveMessageBehaviour(this));
	}
	
	//Members
	private int DISTANCE_DEPLACEMENT;
	private int DISTANCE_PERCEPTION;
	private int CHARGE_MAX;
	
}
