package model;

import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public class BugAgent implements Steppable {

	public BugAgent() {
		super();
		//Initializing the insect capacities
		//Points are randomly given for the two first capacities
		//The last one capacity takes the remaining points
		int remainingPointsToGive = Constants.getInstance().NB_CAPACITIES() - 1;	//We need at least 1 point in perception
		DISTANCE_DEPLACEMENT = 1 + (int)(Math.random() * (Math.min(Constants.getInstance().NB_MAX_DEPLACEMENT(),remainingPointsToGive) - 1));
		remainingPointsToGive -= DISTANCE_DEPLACEMENT;
		CHARGE_MAX = 1 + (int)(Math.random() * (Math.min(Constants.getInstance().NB_MAX_CHARGE(), remainingPointsToGive) - 1));
		remainingPointsToGive -= CHARGE_MAX;
		DISTANCE_PERCEPTION = 1 + remainingPointsToGive;
		VIE_MAX = Constants.getInstance().NB_MAX_VIE();
		
		VIE = VIE_MAX;
		CHARGE = 0;
		
//		System.out.println("!!! New bug !!!");
//		System.out.println("Deplacement : " + DISTANCE_DEPLACEMENT);
//		System.out.println("Charge : " + CHARGE_MAX);
//		System.out.println("Perception : " + DISTANCE_PERCEPTION);
//		System.out.println("Vie max : " + VIE_MAX);
//		System.out.println();
	}
	
	@Override
	public void step(SimState simState) {
		SimulationAgent simulAgent = (SimulationAgent) simState;
		
		//Perception
		//The perception actions are performed with call to methods canEat, canCharge and whereToMove
		
		//System.out.println("VIE : " + VIE + " VIE MAX " + VIE_MAX);
		
		//Choose whether eat or move or charge
		if (canEat(simulAgent)){
			//EAT
			//System.out.println("BUG EAT");
			
			Food food = getMostSuitableFood(simulAgent);	//Looking for a food point
			if (food != null) {
				
				//EAT AT FOOD POINT
				try {
					simulAgent.bugEatAtFoodPoint(this,food);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				
				//EAT CHARGE
				if (CHARGE > 0)
					eatCharge();
			}
			
		} else if (CHARGE < CHARGE_MAX && canCharge(simulAgent)) {
			//CHARGE
			//System.out.println("BUG CHARGE");
			
			try {
				simulAgent.bugChargeFood(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			//MOVE
			//System.out.println("BUG MOVES");
			
			Int2D newLocation = findWhereToMove(simulAgent);	//Looking for a place to move to
			if (newLocation != null){
				try {
					simulAgent.bugMoveToNewLocation(this,newLocation);
				} catch (Exception e) {
					System.out.println("A bug was found on the case");
				}
			}
		}
		
		if (VIE <= 0)
			simulAgent.removeBugAgent(this);
	}
	
	
	

	/**
	 * @param simulAgent
	 * @return
	 * Find a case close enough to move to
	 */
	private Int2D findWhereToMove(SimulationAgent simulAgent) {
		Int2D location = null;
		Int2D bugLocation = getLocation();
		//Bag allObjects = simulAgent.getGrid().getAllObjects();
		
		//Find all the food objects seen
		HashMap<Int2D,Food> foodSeen = new HashMap<Int2D, Food>();
		
		IntBag xbag = new IntBag(), ybag = new IntBag();
		Bag objects = new Bag();
		simulAgent.getGrid().getMooreNeighborsAndLocations(m_x, m_y, DISTANCE_PERCEPTION, Grid2D.TOROIDAL, objects, xbag, ybag);
		for (Object obj : objects){
			if (obj.getClass().isAssignableFrom(Food.class)) {
				Int2D objLocation = simulAgent.getGrid().getObjectLocation(obj); 
				foodSeen.put(objLocation,(Food) obj);
			}
		}
		
		//Strategy of choice
		for (Int2D loc : foodSeen.keySet()){
			boolean hasBug = false;
			
			if (simulAgent.getGrid().numObjectsAtLocation(loc) > 0)
				for (Object obj : simulAgent.getGrid().getObjectsAtLocation(loc)) {
					if (obj.getClass().isAssignableFrom(BugAgent.class)){
						hasBug = true;
						break;
					}
				}
			
			if (hasBug) continue;
			
			//If a case with food, without any bug and close enough is seen, we choose it if it's closer than the previous one chosen
			if (Constants.getInstance().distance(loc, bugLocation) <= DISTANCE_DEPLACEMENT && (location == null || Constants.getInstance().distance(location, bugLocation) < Constants.getInstance().distance(loc, bugLocation)))
				location = loc;
		}
		
		
		
		//No food seen or no food case free
		if (location == null){
			if (foodSeen.isEmpty())
			{
				location = randomPosition();
			} else {
				
				//The food points seen are not reachable but we can find a case adjacent to one
				ArrayList<Int2D> orderedFoodList = new ArrayList<Int2D>();
				orderedFoodList.addAll(foodSeen.keySet());
				boolean valuesSwaped;
				
				//Order the Food from the closest to the farther
				do{
					valuesSwaped = false;
					for (int i = 0; i < orderedFoodList.size() - 1; ++i) {
						if (Constants.getInstance().distance(orderedFoodList.get(i), bugLocation) > Constants.getInstance().distance(orderedFoodList.get(i + 1),bugLocation)){
							Int2D temp = orderedFoodList.get(i);
							orderedFoodList.set(i, orderedFoodList.get(i+1));
							orderedFoodList.set(i+1, temp);
							valuesSwaped = true;
						}
					}
				} while(valuesSwaped);
				
				
				//Find a reachable case
				for (Int2D foodPointLocation : orderedFoodList){					
					
					simulAgent.getGrid().getMooreLocations(foodPointLocation.x, foodPointLocation.y, 1, Grid2D.TOROIDAL, false, xbag, ybag);
					for (int x : xbag.objs){
						for (int y : xbag.objs){
							Int2D currentLocation = new Int2D(x,y);
							if(Constants.getInstance().distance(currentLocation, bugLocation) <= DISTANCE_DEPLACEMENT){
								location = currentLocation;
								
								//We need to check if a bug is not already on the case
								if (simulAgent.getGrid().numObjectsAtLocation(currentLocation) > 0){
									for (Object obj : simulAgent.getGrid().getObjectsAtLocation(currentLocation)){
										if (obj.getClass().isAssignableFrom(BugAgent.class)){
											location = null;
											break;
										}
									}
								}
							}
						}
					}
					
					
					
					
				}
				
				//If we did not find a suitable location, we choose a random location
				if (location == null) {
					location = randomPosition();
				}	
			}
		}
		
		return location;
	}

	
	
	private Int2D randomPosition() {
		Int2D location;
		int newX, newY;
		do{
			int randomDeplacementX = (int)(Math.random() * (2 * DISTANCE_DEPLACEMENT)) - DISTANCE_DEPLACEMENT;
			int randomDeplacementY = (int)(Math.random() * (2 * DISTANCE_DEPLACEMENT)) - DISTANCE_DEPLACEMENT;
			newX = (m_x + randomDeplacementX) % (Constants.getInstance().NB_LINE_AND_COLUMNS() - 1);
			newY = (m_y + randomDeplacementY) % (Constants.getInstance().NB_LINE_AND_COLUMNS() - 1);
		}while (newX == m_x || newY == m_y);
		location = new Int2D(newX >= 0 ? newX : newX + Constants.getInstance().NB_LINE_AND_COLUMNS(), newY >= 0 ? newY : newY + Constants.getInstance().NB_LINE_AND_COLUMNS());
		return location;
	}

	
	
	/**
	 * @param simulAgent
	 * @return The food object where the bug will eat
	 * Looks for the most suitable food point where to eat
	 */
	private Food getMostSuitableFood(SimulationAgent simulAgent) {
		//On choisit le point de nourriture le plus rempli
		
		Food choice = null;
		
		IntBag xbag = new IntBag(), ybag = new IntBag();
		Bag objects = new Bag();
		simulAgent.getGrid().getMooreNeighborsAndLocations(m_x, m_y, 1, Grid2D.TOROIDAL, objects, xbag, ybag);
		for (Object obj : objects){
			if (obj.getClass().isAssignableFrom(model.Food.class) && (choice == null || ((Food) obj).getNumberOfSupplies() > choice.getNumberOfSupplies())){
				choice = (Food) obj;
			}
		}
		
		return choice;
	}

	
	//Possibilities of action
	
	
	/**
	 * @param simulAgent
	 * @return
	 * Check whether a bug can eat or not 
	 */
	private boolean canEat(SimulationAgent simulAgent){
		//If his life is full, the bug does not need to eat
		if (VIE == VIE_MAX){
			return false;
		}
		
		//If there is food on its back, the bug can eat
		if (CHARGE > 0){
			return true;			
		}
		
		//If there is food on an adjacent case, the bug can eat		
		IntBag xbag = new IntBag(), ybag = new IntBag();
		Bag objects = new Bag();
		simulAgent.getGrid().getMooreNeighborsAndLocations(m_x, m_y, 1, Grid2D.TOROIDAL, objects, xbag, ybag);
		for (Object obj : objects){
			if (obj.getClass().isAssignableFrom(model.Food.class)){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * @param simulAgent
	 * @return
	 * Check whether a bug can charge food on its back or not
	 */
	private boolean canCharge(SimulationAgent simulAgent){
		if (CHARGE < CHARGE_MAX)
			if (simulAgent.getGrid().numObjectsAtLocation(m_x, m_y) > 0)
				for(Object obj : simulAgent.getGrid().getObjectsAtLocation(m_x,m_y))
					if (obj.getClass().isAssignableFrom(Food.class))
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
