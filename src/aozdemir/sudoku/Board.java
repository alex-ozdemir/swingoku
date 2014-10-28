package aozdemir.sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Board extends JPanel implements Iterable<Cell>{
	private static final long serialVersionUID = 1L;
	protected static int height = 9;
	protected static int width = 9;
	protected static int numberOfBoxes = 9;
	
	protected Cell[][] cells;
	private Cell selectedCell;
	
	private JPanel[] boxDisplays;
	protected Cell[][] boxes;
	protected Cell[][] columns;
	
	public Board() {
		super();
		this.cells = new Cell[height][width];
		this.columns = new Cell[height][width];
		this.setLayout(new GridLayout(3, 3));
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++) {
				cells[r][c] = new Cell(r, c, this);
				columns[c][r] = cells[r][c];
			}
		this.select(0, 0);
		this.initDisplay();
		this.setSize(new Dimension(width * Cell.SIDE_LENGTH, height * Cell.SIDE_LENGTH));
		this.setVisible(true);
	}
	
	public void select(Cell cell) {
		this.select(cell.row, cell.column);
	}
	
	public void select(int row, int column) {
		if (this.selectedCell != null) this.selectedCell.unSelect();
		this.selectedCell = cells[row][column];
		this.selectedCell.select();
	}
	
	public void initDisplay() {
		this.boxDisplays = new JPanel[numberOfBoxes];
		this.boxes = new Cell[height][width];
		for (int i = 0; i < numberOfBoxes; i++) {
			this.boxDisplays[i] = new JPanel();
			this.boxDisplays[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			this.boxDisplays[i].setLayout(new GridLayout(3, 3));
		}
			
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++) {
				Cell cell = this.cells[r][c];
				this.boxDisplays[cell.getBoxNumber()].add(cell);
				this.boxes[cell.getBoxNumber()][cell.getNumberInBox()] = cell;
			}
		for (int i = 0; i < numberOfBoxes; i++) {
			this.boxDisplays[i].setVisible(true);
			this.add(this.boxDisplays[i]);
		}
	}
	
	protected Cell[] getRow(int r) {
		return cells[r];
	}
	protected Cell[] getColumn(int c) {
		return columns[c];
	}
	protected Cell[] getBox(int b) {
		return boxes[b];
	}
	
	private Cell getRightCell() {
		int newColumn = Math.min(this.selectedCell.column + 1, width - 1);
		return this.cells[this.selectedCell.row][newColumn];
	}
	private Cell getLeftCell() {
		int newColumn = Math.max(this.selectedCell.column - 1, 0);
		return this.cells[this.selectedCell.row][newColumn];
	}
	private Cell getTopCell() {
		int newRow = Math.max(this.selectedCell.row - 1, 0);
		return this.cells[newRow][this.selectedCell.column];
	}
	private Cell getBottomCell() {
		int newRow= Math.min(this.selectedCell.row + 1, height - 1);
		return this.cells[newRow][this.selectedCell.column];
	}
	public void moveCursorLeft() {
		this.select(this.getLeftCell());
	}
	public void moveCursorRight() {
		this.select(this.getRightCell());
	}
	public void moveCursorUp() {
		this.select(this.getTopCell());
	}
	public void moveCursorDown() {
		this.select(this.getBottomCell());
	}
	public void enterCharacter(char c) {
		this.selectedCell.setContent(c);
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			private int r = 0;
			private int c = 0;
			
			public boolean hasNext() {
				return r < height && c < width;
			}
			
			public Cell next() {
				Cell result = cells[r][c];
				if (++c == width) {
					c = 0;
					r++;
				}
				return result;
			}
			
			public void remove() {
				// Don't do this.
			}
		};
	}

}
