package model;

import sim.util.Int2D;

public class Food {

	public Food(){
		this(false);
	}
	
	public Food(boolean isStatic) {
		m_numberOfSupplies = 1 + (int)(Math.random() * Constants.getInstance().NB_MAX_SUPPLIES());
		m_isStatic = isStatic;
	}
	
	void decreaseNumberOfSupplies() throws Exception{
		if (m_numberOfSupplies <= 0)
			throw new Exception();
		--m_numberOfSupplies;
	}
	
	
	//Getters & Setters
	public final int getNumberOfSupplies(){
		return m_numberOfSupplies;
	}
	
	public final boolean isStatic(){
		return m_isStatic;
	}
	
	public final Int2D getLocation(){
		return m_location;
	}
	
	public void setLocation(Int2D loc){
		m_location = loc;
	}
	
	
	
	//Members
	private int m_numberOfSupplies;
	private boolean m_isStatic;
	private Int2D m_location = new Int2D(-1, -1);
	
}
