package model;

import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class BugAgent implements Steppable {

	public BugAgent() {
		super();
		//Initializing the insect capacities
		int remainingPointsToGive = Constants.getInstance().NB_CAPACITIES() - 1;	//We need at least 1 point in perception
		DISTANCE_DEPLACEMENT = 1 + (int)(Math.random() * (Math.min(Constants.getInstance().NB_MAX_DEPLACEMENT(),remainingPointsToGive) - 1));
		remainingPointsToGive -= DISTANCE_DEPLACEMENT;
		CHARGE_MAX = 1 + (int)(Math.random() * (Math.min(Constants.getInstance().NB_MAX_CHARGE(), remainingPointsToGive) - 1));
		remainingPointsToGive -= CHARGE_MAX;
		DISTANCE_PERCEPTION = 1 + remainingPointsToGive;
		VIE_MAX = Constants.getInstance().NB_MAX_VIE();
		
		VIE = VIE_MAX;
		CHARGE = 0;
	}
	
	@Override
	public void step(SimState simState) {
		SimulationAgent simulAgent = (SimulationAgent) simState;
		if (simulAgent == null) System.out.println("NULLLLLLL");
		
		//Perception
		//The perception actions are performed with call to methods canEat, canCharge and whereToMove
		
		
		//Choose whether eat or move or charge
		if (VIE < VIE_MAX && canEat(simulAgent)){
			//EAT
			Food food = getMostSuitableFood(simulAgent);
			if (food == null) {
				//EAT CHARGE
				eatCharge();
			} else {
				//EAT AT FOOD POINT
				try {
					simulAgent.bugEatAtFoodPoint(this,food);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (CHARGE < CHARGE_MAX && canCharge(simulAgent)) {
			//CHARGE
			try {
				simulAgent.bugChargeFood(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//MOVE
			Int2D newLocation = whereToMove(simulAgent);
			if (newLocation != null){
				try {
					simulAgent.bugMoveToNewLocation(this,newLocation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (VIE <= 0)
			simulAgent.removeBugAgent(this);
	}

	private Int2D whereToMove(SimulationAgent simulAgent) {
		Int2D location = null;
		Int2D bugLocation = getLocation();
		Bag allObjects = simulAgent.getGrid().getAllObjects();
		
		//Find all the food objects seen
		HashMap<Int2D,Food> foodSeen = new HashMap<Int2D, Food>();
		if (allObjects != null)	
			for (Object obj : allObjects)
				if (obj instanceof Food) {
					Int2D objLocation = simulAgent.getGrid().getObjectLocation(obj); 
					if (objLocation.x >= m_x - DISTANCE_PERCEPTION || objLocation.x <= m_x - DISTANCE_PERCEPTION)
						if (objLocation.y >= m_y - DISTANCE_PERCEPTION || objLocation.y <= m_y - DISTANCE_PERCEPTION)
							foodSeen.put(objLocation,(Food) obj);
				}
		
		//Strategy of choice
		for (Int2D loc : foodSeen.keySet()){
			Food currentFood = foodSeen.get(loc);
			boolean hasBug = false;
			if (simulAgent.getGrid().numObjectsAtLocation(loc) > 0)
				for (Object obj : simulAgent.getGrid().getObjectsAtLocation(loc)) {
					if (obj instanceof BugAgent){
						hasBug = true;
						break;
					}
				}
			
			if (hasBug) continue;
			
			if (Constants.getInstance().distance(loc, bugLocation) <= DISTANCE_DEPLACEMENT && (location == null || Constants.getInstance().distance(location, bugLocation) < Constants.getInstance().distance(loc, bugLocation)))
				location = loc;
		}
		
		//No food seen or no food place free
		if (location == null){
			if (foodSeen.isEmpty())
			{
				//Find random position
				int randomX = (int)(Math.random() * (m_x + DISTANCE_DEPLACEMENT));
				int randomY = (int)(Math.random() * (m_y + DISTANCE_DEPLACEMENT));
				location = new Int2D(randomX, randomY);
			} else {
				//Find case next to Food
				Int2D foodPointMostClose = null;
				for (Int2D loc : foodSeen.keySet()){
					if (Constants.getInstance().distance(loc, bugLocation) <= DISTANCE_DEPLACEMENT && (location == null || Constants.getInstance().distance(location, bugLocation) < Constants.getInstance().distance(loc, bugLocation)))
						foodPointMostClose = loc;
				}
				
				if (foodPointMostClose == null){
					int randomX = (int)(Math.random() * (m_x + DISTANCE_DEPLACEMENT));
					int randomY = (int)(Math.random() * (m_y + DISTANCE_DEPLACEMENT));
					location = new Int2D(randomX, randomY);
				} else {
				
					for (int i = m_x - 1; i <= m_x + 1 && location == null; ++i){
						for (int j = m_y - 1; j <= m_y + 1 && location == null; ++j){
							if(i >= 0 && j >= 0 && i < Constants.getInstance().NB_LINE_AND_COLUMNS() && j < Constants.getInstance().NB_LINE_AND_COLUMNS()){
								Int2D currentLocation = new Int2D(i,j);
								if(Constants.getInstance().distance(currentLocation, bugLocation) <= DISTANCE_DEPLACEMENT){
									location = currentLocation; 
									if (simulAgent.getGrid().numObjectsAtLocation(foodPointMostClose) > 0)
										for (Object obj : simulAgent.getGrid().getObjectsAtLocation(foodPointMostClose)){
											if (obj instanceof BugAgent){
												location = null;
												break;
											}
										}
								}
							}
									
						}
					}
				}
				
			}
		}
		
		return location;
	}

	private Food getMostSuitableFood(SimulationAgent simulAgent) {
		//On choisit le point de nourriture le plus rempli
		
		if (simulAgent.getGrid() == null) System.out.println("simul null dans suitable");
		
		Food choice = null;
		for (int i = m_x - 1; i <= m_x + 1; ++i)
			for (int j = m_y - 1; j <= m_y + 1; ++j){
				if(i >= 0 && j >= 0 && i < Constants.getInstance().NB_LINE_AND_COLUMNS() && j < Constants.getInstance().NB_LINE_AND_COLUMNS())
					if(i != m_x || j != m_y)
						if (simulAgent.getGrid().numObjectsAtLocation(i,j) > 0)
							for (Object obj : simulAgent.getGrid().getObjectsAtLocation(i,j))
								if (obj instanceof Food) {
									Food food = (Food) obj;
									if (choice == null || food.getNumberOfSupplies() > choice.getNumberOfSupplies())
										choice = food;
								}
			}
		
		return choice;
	}

	//Possibilities of action
	private boolean canEat(SimulationAgent simulAgent){
		if (CHARGE > 0){
			return true;			
		}
		
		if (simulAgent == null) System.out.println("simul null");
		
		for (int i = m_x - 1; i <= m_x + 1; ++i)
			for (int j = m_y - 1; j <= m_y + 1; ++j){
				if(i >= 0 && j >= 0 && i < Constants.getInstance().NB_LINE_AND_COLUMNS() && j < Constants.getInstance().NB_LINE_AND_COLUMNS())
					if(i != m_x || j != m_y)
						if (simulAgent.getGrid().numObjectsAtLocation(i,j) > 0)
							for (Object obj : simulAgent.getGrid().getObjectsAtLocation(i,j))
								if (obj instanceof Food)
									return true;
			}
		return false;
	}
	
	
	private boolean canCharge(SimulationAgent simulAgent){
		if (CHARGE < CHARGE_MAX)
			if (simulAgent.getGrid().numObjectsAtLocation(m_x, m_y) > 0)
				for(Object obj : simulAgent.getGrid().getObjectsAtLocation(m_x,m_y))
					if (obj instanceof Food)
						return true;
		return false;
	}
	
	
	//Action performed
	
	private void eatCharge() {
		try {
			increaseVie(Constants.getInstance().NB_ENERGY());
			decreaseCharge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void increaseCharge() throws Exception {
		if (CHARGE >= CHARGE_MAX)
			throw new Exception();
		++CHARGE;
	}
	
	public void decreaseCharge() throws Exception {
		if (CHARGE <= 0)
			throw new Exception();
		--CHARGE;
	}
	
	public void increaseVie() throws Exception {increaseVie(1);}
	public void increaseVie(int value) throws Exception
	{
		if (VIE >= VIE_MAX)
			throw new Exception();
		VIE += value;
		if (VIE > VIE_MAX)
			VIE = VIE_MAX;
	}
	
	public void decreaseVie() throws Exception {
		if (VIE <= 0)
			throw new Exception();
		--VIE;
	}
	
	//Getters & Setters
	public int x(){return m_x;}
	public int y(){return m_y;}
	public void setX(int x){m_x = x;}
	public void setY(int y){m_y=y;}
	
	public Int2D getLocation() {
		return new Int2D(m_x,m_y);
	}
	
	public Stoppable getStoppable() {
		return m_stoppable;
	}

	public void setStoppable(Stoppable m_stoppable) {
		this.m_stoppable = m_stoppable;
	}

	//Members
	private int DISTANCE_DEPLACEMENT;
	private int DISTANCE_PERCEPTION;
	private int CHARGE_MAX;
	private int VIE_MAX;
	private int VIE;
	private int CHARGE;
	
	private int m_x;
	private int m_y;
	
	private Stoppable m_stoppable;
	
}
