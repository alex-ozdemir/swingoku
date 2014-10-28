package aozdemir.sudoku;

import java.util.LinkedList;

public class ColorBoard extends Board {
	private static final long serialVersionUID = 1L;
	protected static final LinkedList<Character> possibleEntries = new LinkedList<Character>();
	static { setupPossibleEntries(); }
	
	protected static void setupPossibleEntries() {
		possibleEntries.clear();
		for(int i = 1; i < 10; i++) possibleEntries.add(Character.forDigit(i, 10));
	}
	

	public ColorBoard() {
		super();
	}
	
	public void handleColors() {
		clearColors();
		if (!check() && filled())
			reportSuccess();
	}
	
	public void setContents(char[] cs) {
		int i = 0;
		for (Cell c : this) {
			c.setContent(cs[i]);
			++i;
		}
		handleColors();
	}
	
	protected boolean filled() {
		for (Cell cell : this)
			if (cell.content == ' ')
				return false;
		return true;
	}


	private boolean check(){
		return checkRows() | checkColumns() | checkBoxes();
	}


	private boolean checkRows() {
		boolean conflict = false;
		for (int r = 0; r < height; r++)
			conflict |= checkRow(r);
		return conflict;
	}
	
	private boolean checkRow(int r) {
		setupPossibleEntries();
		for(int c = 0; c < width; c++) {
			if (cells[r][c].content != ' ')	{	
				if (possibleEntries.contains(cells[r][c].content)) {
					possibleEntries.remove(new Character(cells[r][c].content));
				} else {
					alertRow(r);
					return true;
				}
			}
		}
		return false;
	}

	private void alertRow(int r) {
		for (Cell cell : cells[r])
			cell.alertBackground();
	}

	private boolean checkColumns() {
		boolean conflict = false;
		for (int c = 0; c < width; c++)
			conflict |= checkColumn(c);
		return conflict;
	}

	private boolean checkColumn(int c) {
		setupPossibleEntries();
		for(int r = 0; r < width; r++) {
			if (cells[r][c].content != ' ')	{	
				if (possibleEntries.contains(cells[r][c].content)) {
					possibleEntries.remove(new Character(cells[r][c].content));
				} else {
					alertColumn(c);
					return true;
				}
			}
		}
		return false;
	}
	
	private void alertColumn(int c) {
		for (int r = 0; r < height; r++)
			cells[r][c].alertBackground();
	}

	private boolean checkBoxes() {
		boolean newInformation = false;
		for (int c = 0; c < numberOfBoxes; c++)
			newInformation |= checkBox(c);
		return newInformation;		
	}
	
	private boolean checkBox(int b) {
		setupPossibleEntries();
		Cell[] box = boxes[b];
		for (int i = 0; i < box.length; i++) {
			if (box[i].content != ' ') {	
				if (possibleEntries.contains(box[i].content)) {
					possibleEntries.remove(new Character(box[i].content));
				} else {
					alertBox(b);
					return true;
				}
			}
		}
		return false;	
	}

	private void alertBox(int b) {
		for (int i = 0; i < height * width / numberOfBoxes; i++)
			this.boxes[b][i].alertBackground();
	}
	
	private void clearColors() {
		for (Cell cell : this)
			cell.clearBackground();
	}

	private void reportSuccess() {
		for (Cell cell : this)
			cell.successBackground();
	}
}
