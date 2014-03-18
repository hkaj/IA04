package view;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import model.ChatAgent;

public class ChatWindow extends JFrame implements PropertyChangeListener {

	public ChatWindow(ChatAgent agent) throws HeadlessException {
		this("", agent);
	}

	public ChatWindow(String arg0, ChatAgent agent) throws HeadlessException {
		super(arg0);
		m_agent = agent;
		// TODO Auto-generated constructor stub
	}
	
	
	
	private ChatAgent m_agent;



	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
