package simulation;

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

	public final int NB_FOOD() {
		return NB_FOOD;
	}

	public final int NB_MAX_DEPLACEMENT() {
		return NB_MAX_DEPLACEMENT;
	}

	public final int NB_MAX_CHARGE() {
		return NB_MAX_CHARGE;
	}

	public final int NB_MAX_PERCEPTION() {
		return NB_MAX_PERCEPTION;
	}

	//Members
	private int NB_LINE_AND_COLUMNS = 10;
	private int NB_BUGS = 4;
	private int NB_FOOD = 12;
	
	private int NB_MAX_DEPLACEMENT = 3;
	private int NB_MAX_CHARGE = 2;
	private int NB_MAX_PERCEPTION = 5;
}
