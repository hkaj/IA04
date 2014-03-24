package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import view.ChatWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ChatAgent extends Agent {

	@Override
	protected void setup() {
		//G�n�ration de la Fenetre
		@SuppressWarnings("unused")
		ChatWindow chatWindow = new ChatWindow("", this);
		addBehaviour(new ChatBehaviourReceiveMessage(this));
		
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
	
	public void addNewMessage(String text, AID agent) {
		if (text != ""){
			String old = currentText;
			currentText += agent.getLocalName() + " says : ";
			currentText += text + "\n";
			
			if (agent == getAID()){
				addBehaviour(new ChatBehaviourSendMessage(this, text));
			}
			
			firePropertyChange("text_content", old, currentText);
		}
	}
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
	private String currentText = "";
}
