package view;

import java.awt.Color;

import javax.swing.JFrame;

import model.BugAgent;
import model.Food;
import model.SimulationAgent;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class Visualisation extends GUIState {

	private static final int FRAME_SIZE = 400;
	
	public Visualisation(SimulationAgent state) {
		super(state);
		m_simulationAgent = state;
	}
	
	public void init(Controller c) {
		//Init the window (frame)
		super.init(c);
		m_display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		m_display.setClipping(false);
		m_displayFrame = m_display.createFrame();
		m_displayFrame.setTitle("Bugs");
		c.registerFrame(m_displayFrame);
		m_displayFrame.setVisible(true);
		m_display.attach( m_gridPortrayal, "Insect Grid" );
	}
	
	
	public void setupPortrayals() {
		//Setting the representation for each component
		m_gridPortrayal.setField(m_simulationAgent.getGrid());
		m_gridPortrayal.setPortrayalForClass(BugAgent.class, getBugAgentPortrayal());
		m_gridPortrayal.setPortrayalForClass(Food.class, getFoodPortrayal());
		m_display.reset();
		m_display.setBackdrop(Color.yellow);
		m_display.repaint();
	}
	
	
	private OvalPortrayal2D getBugAgentPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.RED;
		r.filled = true;
		return r;
	}
	
	
	private OvalPortrayal2D getFoodPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.GREEN;
		r.filled = true;
		return r;
	}


	public static String getName(){
		return "Simulation d'insectes";
	}
	
	
	//Create Inspector
	public Object getSimulationInspectedObject() {return state;}
	
	public Inspector getInspector() {
		Inspector insp = super.getInspector();
		insp.setVolatile(true);
		return insp;
	}
	
	
	//Getters & Setters
	public int getNumBugs() {
		return m_nbOfBugs;
	}
	
	
	public void setNumBugs(int nb) {
		m_nbOfBugs = nb;
	}

	
	//Members
	private Display2D m_display;
	private JFrame m_displayFrame;
	private SparseGridPortrayal2D m_gridPortrayal = new SparseGridPortrayal2D();
	private SimulationAgent m_simulationAgent;
	private int m_nbOfBugs;
}
