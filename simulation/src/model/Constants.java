package model;

import sim.util.Int2D;

public class Constants {
	
	private Constants(){
		
	}
	
	private static Constants instance = null;
	
	public static Constants getInstance() {
		if (instance == null)
			instance = new Constants();
		return instance;
	}
	
	public static void releaseInstance() {
		instance = null;
	}
	
	
	//Methods
	public final int NB_LINE_AND_COLUMNS() {
		return NB_LINE_AND_COLUMNS;
	}

	public final int NB_BUGS() {
		return NB_BUGS;
	}

	public final int NB_FOOD_CELL() {
		return NB_FOOD_CELL;
	}

	public final int NB_MAX_DEPLACEMENT() {
		return NB_MAX_DEPLACEMENT;
	}

	public final int NB_MAX_CHARGE() {
		return NB_MAX_CHARGE;
	}

	public final int NB_MAX_VIE() {
		return NB_MAX_VIE;
	}
	
	public final int NB_CAPACITIES(){
		return NB_CAPACITIES;
	}
	
	public final int NB_MAX_SUPPLIES() {
		return NB_MAX_SUPPLIES;
	}
	
	public final int NB_ENERGY(){
		return NB_ENERGY;
	}
	
	
	//Tools
	public int distance(Int2D a, Int2D b){
		int numberOfXCase = Math.abs(a.x - b.x);
		int numberOfYCase = Math.abs(a.y - b.y);
		return Math.max( numberOfXCase , numberOfYCase );
	}
	
	
	//Members
	private int NB_LINE_AND_COLUMNS = 30;
	private int NB_BUGS = 10;
	private int NB_FOOD_CELL = 15;
	
	//Properties for bugs
	private int NB_MAX_DEPLACEMENT = 5;
	private int NB_MAX_CHARGE = 5;
	private int NB_CAPACITIES = 10;
	private int NB_MAX_VIE = 15;

	//Properties for food
	private int NB_MAX_SUPPLIES = 5;
	private int NB_ENERGY = 3;
	
}
