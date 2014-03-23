package view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import model.ChatAgent;

public class ChatWindow extends JFrame implements PropertyChangeListener {

	public ChatWindow(ChatAgent agent) throws HeadlessException {
		this("", agent);
	}

	public ChatWindow(String arg0, ChatAgent agent) throws HeadlessException {
		super(arg0);
		m_agent = agent;
		m_agent.addPropertyChangeListener(this);
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		//North
		m_quitSubMenu = new JMenuItem("Fermer");
		m_fileMenu = new JMenu("Fichier");
		m_fileMenu.add(m_quitSubMenu);
		m_menu = new JMenuBar();
		m_menu.add(m_fileMenu);
		add(m_menu, BorderLayout.NORTH);
		
		m_quitSubMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		
		//Center
		add(m_chatBox, BorderLayout.CENTER);
		
		//South
		add(m_chatInput, BorderLayout.SOUTH);
		add(m_sendButton, BorderLayout.SOUTH);
		
		m_sendButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				m_agent.addNewMessage(m_chatInput.getText());
				m_chatInput.setText("");
			}
		});
		
		//Disposition
		setSize(500, 900);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
	}	
	
	private ChatAgent m_agent;
	
	//Center
	private JTextArea m_chatBox = new JTextArea();
	
	//South
	private JTextArea m_chatInput = new JTextArea(1,0);
	private JButton m_sendButton = new JButton("Envoyer");
	
	//North
	private JMenuBar m_menu;
	private JMenu m_fileMenu;
	private JMenuItem m_quitSubMenu;
}
