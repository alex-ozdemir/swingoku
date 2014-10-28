package aozdemir.sudoku;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import aozdemir.sudoku.exceptions.NoSolutionException;

public class App extends JFrame implements KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private static final String SOLVE = "SOLVECODE";
	private static final String LOAD = "LOADCODE";
	private static final String SAVE = "SAVECODE";
	
	private SolveBoard b; 
	private JLabel message;
	private JPanel container;
	private JFileChooser fileDialog;
	
	public App() {
		super("Sudoku");
		container = new JPanel(new BorderLayout());
		fileDialog = new JFileChooser();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createBoard();
		this.createToolbar();
		this.setLayout(new GridLayout(1,2));
		this.add(container);
		this.pack();
		this.addKeyListener(this);
		container.setVisible(true);
		this.setVisible(true);
	}
	
	private void createToolbar() {
		JToolBar toolbar = new JToolBar();
		createSolveButton(toolbar);
		createLoadButton(toolbar);
		createSaveButton(toolbar);
		createMessageDisplay(toolbar);
		container.add(toolbar, BorderLayout.PAGE_START);
	}

	private void createMessageDisplay(JToolBar toolbar) {
		message = new JLabel();
		toolbar.add(message);
	}

	private void createSolveButton(JToolBar toolbar) {
		JButton solve = new JButton("Solve");
		solve.setFocusable(false);
		solve.setActionCommand(SOLVE);
		solve.addActionListener(this);
		toolbar.add(solve);
	}
	
	private void createLoadButton(JToolBar toolbar) {
		JButton load = new JButton("Load");
		load.setFocusable(false);
		load.setActionCommand(LOAD);
		load.addActionListener(this);
		toolbar.add(load);
	}
	
	private void createSaveButton(JToolBar toolbar) {
		JButton save = new JButton("Save");
		save.setFocusable(false);
		save.setActionCommand(SAVE);
		save.addActionListener(this);
		toolbar.add(save);
	}

	private void createBoard() {
		this.b = new SolveBoard(
				new char[][] {
						new char[]{'5', '3', ' ', ' ', '7', ' ', ' ', ' ', ' '},
						new char[]{'6', ' ', ' ', '1', '9', '5', ' ', ' ', ' '},
						new char[]{' ', '9', '8', ' ', ' ', ' ', ' ', '6', ' '},
						new char[]{'8', ' ', ' ', ' ', '6', ' ', ' ', ' ', '3'},
						new char[]{'4', ' ', ' ', '8', ' ', '3', ' ', ' ', '1'},
						new char[]{'7', ' ', ' ', ' ', '2', ' ', ' ', ' ', '6'},
						new char[]{' ', '6', ' ', ' ', ' ', ' ', '2', '8', ' '},
						new char[]{' ', ' ', ' ', '4', '1', '9', ' ', ' ', '5'},
						new char[]{' ', ' ', ' ', ' ', '8', ' ', ' ', '7', '9'}
				});
		this.setMinimumSize(b.getSize());
		container.add(b, BorderLayout.CENTER);
	}
	
	private void displayMessage(String msg) {
		message.setText(msg);
	}
	
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		@SuppressWarnings("unused")
		App app = new App();
	}

	
	public void keyPressed(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_S:
				try {
					b.solve();
				} catch (NoSolutionException e1) {
					displayMessage("No Solution");
				}
				break;
			case Controls.UP:
				b.moveCursorUp();
				break;
			case Controls.DOWN:
				b.moveCursorDown();
				break;
			case Controls.LEFT:
				b.moveCursorLeft();
				break;
			case Controls.RIGHT:
				b.moveCursorRight();
				break;
			case Controls.EMPTY:
				b.enterCharacter(' ');
				b.handleColors();
				break;
			}
			if (Controls.NUMBERS.contains(e.getKeyCode())) {
				b.enterCharacter(e.getKeyChar());
				b.handleColors();
			}
		}		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String commandCode = e.getActionCommand();
		
		if(SOLVE.equals(commandCode)) {
			try{
				b.solve();
			} catch (NoSolutionException ex) {
				displayMessage("No Solution Possible");
			}
		} else if (LOAD.equals(commandCode)) {
			int returnCode = fileDialog.showOpenDialog(App.this);
			if (returnCode == JFileChooser.APPROVE_OPTION) {
				try {
					File f = fileDialog.getSelectedFile();
					BufferedReader r = new BufferedReader(new FileReader(f));
					b.setContents(r.readLine().toCharArray());
					r.close();
					displayMessage("Loaded");
				} catch (IOException e1) {
					displayMessage("Could not load from file");
				}
			}
		} else if (SAVE.equals(commandCode)) {
			int returnCode = fileDialog.showSaveDialog(App.this);
			if (returnCode == JFileChooser.APPROVE_OPTION) {
				StringBuffer record = new StringBuffer();
				for (Cell c : b)
					record.append(c.content);
				try {
					File f = fileDialog.getSelectedFile();
					FileWriter writer = new FileWriter(f);
					writer.write(record.toString());
					writer.close();
					displayMessage("Saved");
				} catch (IOException e1) {
					displayMessage("Could not save to file");
				}
			}
		}
		
		this.requestFocusInWindow();
		
	}
}
