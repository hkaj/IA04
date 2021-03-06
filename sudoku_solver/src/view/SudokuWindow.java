package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import sudoku_solver.EnvAgent;

public class SudokuWindow extends JFrame implements PropertyChangeListener {

	public SudokuWindow(EnvAgent agent) throws HeadlessException {
		this("", agent);
	}

	public SudokuWindow(String title, EnvAgent agent) throws HeadlessException {
		super(title);
		
		m_agent = agent;
		if (m_agent != null){
			m_agent.addPropertyChangeListener(this);
		}
		
		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);
		
		//Menu
		m_quitSubMenu = new JMenuItem("Quitter");
		m_fileMenu = new JMenu("Fichier");
		m_fileMenu.add(m_quitSubMenu);
		m_menuBar = new JMenuBar();
		m_menuBar.add(m_fileMenu);
		
		m_quitSubMenu.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
		}});
		
		add(m_menuBar, BorderLayout.NORTH);
		
		//Center Frame
		GridLayout centerLayout = new GridLayout(sizeOfSudoku, sizeOfSudoku);
		m_centerPanel = new JPanel(centerLayout);
		m_sudokuArrayLabels = new JLabel[sizeOfSudoku][sizeOfSudoku];
		for (int lig = 0; lig < sizeOfSudoku; ++lig)
			for (int col = 0; col < sizeOfSudoku; ++col){
				if (m_agent != null){
					int value = m_agent.getCaseValue(lig, col);
					m_sudokuArrayLabels[lig][col] = new JLabel(Integer.toString(value), SwingConstants.CENTER);
					m_sudokuArrayLabels[lig][col].setFont(new Font("Serif", Font.PLAIN, 20));
					if (value == 0)
						m_sudokuArrayLabels[lig][col].setForeground(Color.red);
					else
						m_sudokuArrayLabels[lig][col].setForeground(Color.black);
				}
				else
					m_sudokuArrayLabels[lig][col] = new JLabel("0", SwingConstants.CENTER);
				setLabelBorderColor(lig, col);
				m_centerPanel.add(m_sudokuArrayLabels[lig][col]);
			}
		add(m_centerPanel, BorderLayout.CENTER);
		
		//JFrame configuration
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 450);
		setLocation(400, 400);
		setVisible(true);
	}
	
	
	private void setLabelBorderColor(int x, int y){	
		JLabel label = m_sudokuArrayLabels[x][y];
		int top = 0, bottom = 0, left = 0, right = 0;
		
		if (x%3 == 0 && x != 0 && x != sizeOfSudoku - 1) top = 1; else top = 0;
		if ((x+1)%3 == 0 && x != 0 && x != sizeOfSudoku - 1) bottom = 1; else bottom = 0;
		if (y%3 == 0 && y != 0 && y != sizeOfSudoku - 1) left = 1; else left = 0;
		if ((y+1)%3 == 0 && y != 0 && y != sizeOfSudoku - 1) right = 1; else right = 0;
		
		label.setBorder(new MatteBorder(top, left, bottom, right, Color.red));
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		SudokuWindow win = new SudokuWindow("Sudoku Window Test", null);
	}

	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "Case_changed"){
			int[] args = (int[]) evt.getNewValue();
			int lig = (int) args[0];
			int col = (int) args[1];
			int value = (int) args[2];
			m_sudokuArrayLabels[lig][col].setText(Integer.toString(value));
			if (value != 0)
				m_sudokuArrayLabels[lig][col].setForeground(Color.black);
		}
	}
	
	
	
	//Members
	EnvAgent m_agent;
	
	static final int sizeOfSudoku = 9;
	JMenuBar m_menuBar;
	JMenu m_fileMenu;
	JMenuItem m_quitSubMenu;
	JLabel m_sudokuArrayLabels[][];
	JPanel m_centerPanel;

}
