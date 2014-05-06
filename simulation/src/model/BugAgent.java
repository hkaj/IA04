package model;

import sim.engine.SimState;
import sim.engine.Steppable;

public class BugAgent implements Steppable {

	public BugAgent() {
		super();
		//Initializing the insect capacities
		DISTANCE_DEPLACEMENT = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_DEPLACEMENT());
		DISTANCE_PERCEPTION = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_PERCEPTION());
		CHARGE_MAX = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_CHARGE());
		VIE_MAX = Constants.getInstance().NB_MAX_VIE();
		
		VIE = VIE_MAX;
		CHARGE = 0;
	}
	
	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub
		
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
	
	public void move(SimulationAgent simulAgent){
		//TODO
	}
	
	//Getters & Setters
	public int x(){return m_x;}
	public int y(){return m_y;}
	public void setX(int x){m_x = x;}
	public void setY(int y){m_y=y;}
	
	//Members
	private int DISTANCE_DEPLACEMENT;
	private int DISTANCE_PERCEPTION;
	private int CHARGE_MAX;
	private int VIE_MAX;
	private int VIE;
	private int CHARGE;
	
	private int m_x;
	private int m_y;
	
}
