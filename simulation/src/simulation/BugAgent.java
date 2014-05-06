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
		VIE_MAX = Constants.getInstance().NB_MAX_VIE();
		
		VIE = VIE_MAX;
		CHARGE = 0;
		
		//Add Behaviours
		addBehaviour(new BugReceiveMessageBehaviour(this));
	}
	
	public void increaseCharge() throws Exception
	{
		if (CHARGE >= CHARGE_MAX)
			throw new Exception();
		++CHARGE;
	}
	
	public void decreaseCharge() throws Exception {
		if (CHARGE <= 0)
			throw new Exception();
		--CHARGE;
	}
	
	public void increaseVie() throws Exception
	{
		if (VIE >= VIE_MAX)
			throw new Exception();
		++VIE;
	}
	
	public void decreaseVie() throws Exception {
		if (VIE <= 0)
			throw new Exception();
		--VIE;
	}
	
	//Members
	private int DISTANCE_DEPLACEMENT;
	private int DISTANCE_PERCEPTION;
	private int CHARGE_MAX;
	private int VIE_MAX;
	private int VIE;
	private int CHARGE;
	
}
