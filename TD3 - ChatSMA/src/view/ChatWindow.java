package view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		BorderLayout northLayout = new BorderLayout();
		m_northPanel.setLayout(northLayout);
		m_quitSubMenu = new JMenuItem("Fermer");
		m_fileMenu = new JMenu("Fichier");
		m_fileMenu.add(m_quitSubMenu);
		m_menu = new JMenuBar();
		m_menu.add(m_fileMenu);
		m_northPanel.add(m_menu, BorderLayout.NORTH);
		m_userLabel.setText("Vous êtes " + m_agent.getLocalName());
		m_northPanel.add(m_userLabel, BorderLayout.CENTER);
		add(m_northPanel, BorderLayout.NORTH);
		
		m_quitSubMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		
		//Center
		add(m_chatPane, BorderLayout.CENTER);
		m_chatBox.setEditable(false);
		
		//South
		add(m_southPanel, BorderLayout.SOUTH);
		BorderLayout layoutSouthPanel = new BorderLayout();
		m_southPanel.setLayout(layoutSouthPanel);
		m_southPanel.add(m_chatInput, BorderLayout.CENTER);
		m_southPanel.add(m_sendButton, BorderLayout.EAST);
		
		m_sendButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				m_agent.addNewMessage(m_chatInput.getText(), m_agent.getAID());
				m_chatInput.setText("");
			}
		});
		
		//Disposition
		setSize(500, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt != null){
			if (evt.getPropertyName() == "text_content"){
				m_chatBox.setText((String) evt.getNewValue());
			}
		}
	}	
	
	private ChatAgent m_agent;
	
	//Center
	private JTextArea m_chatBox = new JTextArea();
	private JScrollPane m_chatPane = new JScrollPane(m_chatBox);
	
	//South
	private JPanel m_southPanel = new JPanel();
	private JTextField m_chatInput = new JTextField();
	private JButton m_sendButton = new JButton("Envoyer");
	
	//North
	private JPanel m_northPanel = new JPanel();
	private JMenuBar m_menu;
	private JMenu m_fileMenu;
	private JMenuItem m_quitSubMenu;
	private JLabel m_userLabel = new JLabel();
}
