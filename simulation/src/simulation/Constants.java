package simulation;

public class Constants {

	public int NB_LINE_AND_COLUMNS = 10;
	public int NB_BUGS = 4;
	public int NB_FOOD = 12;
	
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
}
