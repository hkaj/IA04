package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import view.ChatWindow;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ChatAgent extends Agent {

	@Override
	protected void setup() {
		//G�n�ration de la Fenetre
		ChatWindow chatWindow = new ChatWindow("", this);
		addBehaviour(new ChatBehaviourReceiveMessage(this));
		addBehaviour(new ChatBehaviourSendMessage(this));
		
		//Registering to the AMS list
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Chat");
				sd.setName("User");
				dfd.addServices(sd);
				try{
					DFService.register(this, dfd);
				} catch (FIPAException e){
					e.printStackTrace();
				}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void addNewMessage(String text) {
		// TODO Auto-generated method stub	
	}
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
}
