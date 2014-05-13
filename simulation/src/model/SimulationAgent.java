package model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class SimulationAgent extends SimState {

	public SimulationAgent(long seed) {
		super(seed);
		
		m_grid = new SparseGrid2D(Constants.getInstance().NB_LINE_AND_COLUMNS(), Constants.getInstance().NB_LINE_AND_COLUMNS());
	}

	public void start (){
		super.start();
		m_grid.clear();
		
		//Remplissage de la grille avec les insectes et la nourriture
		addBugAgents();
		addFoods();
	}

	private void addBugAgents() {
		for(int i = 0; i < Constants.getInstance().NB_BUGS(); i++) {
			BugAgent bug = new BugAgent();
			Int2D location = getFreeLocation(bug);
			m_grid.setObjectLocation(bug, location.x, location.y);
			bug.setX(location.x);
			bug.setY(location.y);
			Stoppable stoppable = schedule.scheduleRepeating(bug);
			bug.setStoppable(stoppable);
		}
	}
	
	
	private void addFoods() {
		for(int i = 0; i < Constants.getInstance().NB_FOOD_CELL(); ++i){
			addFood();
		}		
	}
	
	
	private void addFood() {
		Food food = new Food(true);
		Int2D location = getFreeLocation(food);
		m_grid.setObjectLocation(food, location);
		food.setLocation(location);		
	}
	

	public Int2D getFreeLocation(Object objToAdd) {
		//Trouve une case libre adjacente où se déplacer
		//Une case ne contenant pas d'autre object du même type que objToAdd est éligible
		Int2D location = null;
		do{
			location = new Int2D(random.nextInt(m_grid.getWidth()), random.nextInt(m_grid.getHeight()));
			Bag objBag = m_grid.getObjectsAtLocation(location.x,location.y);
			if (objBag != null){
				for (Object obj : objBag){
					if (obj.getClass().isAssignableFrom(objToAdd.getClass()) || objToAdd.getClass().isAssignableFrom(obj.getClass())){
						location = null;
						break;
					}
				}
			}
		} while (location == null);
		return location;
	}
	
	
	
	//Bug actions
	public void bugEatAtFoodPoint(BugAgent bugAgent, Food food) throws Exception {
		if (food.getNumberOfSupplies() < 0)
			throw new Exception();
		
		//Bug eats
		bugAgent.increaseVie(Constants.getInstance().NB_ENERGY());
		food.decreaseNumberOfSupplies();
		
		//Need to create new food point if supplies are empty
		if (food.getNumberOfSupplies() == 0) {
			m_grid.remove(food);
			addFood();
		}
	}
	
	public void bugChargeFood(BugAgent bugAgent) throws Exception {
		//Find the food object
		Food food = null;
		for (Object obj : m_grid.getObjectsAtLocation(bugAgent.x(), bugAgent.y()))
				if (obj instanceof Food)
					food = (Food) obj;
		
		//Bug charge
		bugAgent.increaseCharge();
		food.decreaseNumberOfSupplies();
		
		//Need to create new food point if supplies are empty
		if (food.getNumberOfSupplies() == 0) {
			m_grid.remove(food);
			addFood();
		}
	}	
	
	public void bugMoveToNewLocation(BugAgent bugAgent, Int2D newLocation) throws Exception {
		//Verify if the new location is available first
		for (Object obj : m_grid.getObjectsAtLocation(newLocation))
				if (obj instanceof BugAgent)
					throw new Exception();
		
		//Move the bug
		m_grid.remove(bugAgent);
		m_grid.setObjectLocation(bugAgent, newLocation);
		bugAgent.decreaseVie();
	}
	
	public void removeBugAgent(BugAgent bug){
		m_grid.remove(bug);
		bug.getStoppable().stop();
	}
	
	
	//Getters & Setters
	public final SparseGrid2D getGrid() { return m_grid;}
	public final void setGrid(SparseGrid2D m_grid) { this.m_grid = m_grid; }

	//Members
	private SparseGrid2D m_grid;

}
