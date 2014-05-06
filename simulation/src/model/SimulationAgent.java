package model;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import jade.core.Agent;

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
		//TODO
	}
	
	private void addBugAgents() {
		for(int i = 0; i < Constants.getInstance().NB_BUGS(); i++) {
			BugAgent bug = new BugAgent();
			Int2D location = getFreeLocation(bug);
			m_grid.setObjectLocation(bug, location.x, location.y);
			bug.setX(location.x);
			bug.setY(location.y);
			schedule.scheduleRepeating(bug);
		}
	}

	
	private Int2D getFreeLocation(Object objToAdd) {
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
	
	//Getters & Setters
	public final SparseGrid2D getGrid() { return m_grid;}
	public final void setGrid(SparseGrid2D m_grid) { this.m_grid = m_grid; }

	//Members
	private SparseGrid2D m_grid;
}
