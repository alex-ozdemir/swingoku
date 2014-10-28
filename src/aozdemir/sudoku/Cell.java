package aozdemir.sudoku;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Cell extends JLabel{
	private static final long serialVersionUID = -2105319664967507044L;
	
	public static final int SIDE_LENGTH = 60;
	private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(new Color(200, 200, 200));
	private static final Border SELECT_BORDER = BorderFactory.createLineBorder(Color.BLUE, 2);
	
	private static final Color ALERT_BACKGROUND = new Color(255, 230, 230);
	private static final Color SUCCESS_BACKGROUND = new Color(230, 255, 230);
	private static final Color CLEAR_BACKGROUND = Color.WHITE;
	
	char content;
	
	private Board board;
	protected int row;
	protected int column;
		
	public Cell(int row, int column, Board board) {
		super(" ");
		this.setOpaque(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.row = row;
		this.column = column;
		this.board = board;
		this.content = ' ';
		this.setBorder(DEFAULT_BORDER);
		
		final Cell self = this;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				self.board.select(self.row, self.column);
			}
		});
	}
	
	public void setContent(char content) {
		this.content = content;
		this.setText("" + content);
	}
	
	public void select() {
		this.setBorder(SELECT_BORDER);
	}
	
	public void unSelect() {
		this.setBorder(DEFAULT_BORDER);
	}
	
	public int getBoxNumber() {
		return this.row / 3 * 3 + this.column / 3;
	}
	
	public int getNumberInBox() {
		return this.row % 3 * 3 + this.column % 3;
	}
	
	public void clearBackground() {
		this.setBackground(CLEAR_BACKGROUND);
	}

	public void successBackground() {
		this.setBackground(SUCCESS_BACKGROUND);
	}

	public void alertBackground() {
		this.setBackground(ALERT_BACKGROUND);
	}
}
