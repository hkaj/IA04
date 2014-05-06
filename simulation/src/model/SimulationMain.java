package model;

import sim.display.Console;
import view.Visualisation;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SimulationMain {
	
	public static void main(String[] args) {
		runUI();
	}
	
	public static void runUI() {
		SimulationAgent model = new SimulationAgent(System.currentTimeMillis());
		Visualisation gui = new Visualisation(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}


}
