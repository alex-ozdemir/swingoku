package aozdemir.sudoku;

import java.util.HashMap;
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
		boolean result = false;
		HashMap<Character, Integer> entryLocations = new HashMap<Character, Integer>();
		for(int c = 0; c < width; c++) {
			char ch = cells[r][c].content;
			if (ch != ' ')	{
				if (!entryLocations.containsKey(ch))
					entryLocations.put(ch, c);
				else {
					cells[r][c].alertBackground();
					Cell duplicate = cells[r][entryLocations.get(ch)];
					duplicate.alertBackground();
					result = true;
				}
			}
		}
		return result;
	}

	private boolean checkColumns() {
		boolean conflict = false;
		for (int c = 0; c < width; c++)
			conflict |= checkColumn(c);
		return conflict;
	}

	private boolean checkColumn(int c) {
		boolean result = false;
		HashMap<Character, Integer> entryLocations = new HashMap<Character, Integer>();
		for(int r = 0; r < width; r++) {
			char ch = cells[r][c].content;
			if (ch != ' ')	{	
				if (!entryLocations.containsKey(ch))
					entryLocations.put(ch, r);
				else {
					cells[r][c].alertBackground();
					Cell duplicate = cells[entryLocations.get(ch)][c];
					duplicate.alertBackground();
					result = true;
				}
			}
		}
		return result;
	}

	private boolean checkBoxes() {
		boolean newInformation = false;
		for (int c = 0; c < numberOfBoxes; c++)
			newInformation |= checkBox(c);
		return newInformation;		
	}
	
	private boolean checkBox(int b) {
		boolean result = false;
		HashMap<Character, Integer> entryLocations = new HashMap<Character, Integer>();
		Cell[] box = boxes[b];
		for (int i = 0; i < box.length; i++) {
			Cell cell = box[i];
			char ch = cell.content;
			if (ch != ' ')	{	
				if (!entryLocations.containsKey(ch))
					entryLocations.put(ch, i);
				else {
					cells[cell.row][cell.column].alertBackground();
					Cell duplicate = box[entryLocations.get(ch)];
					duplicate.alertBackground();
					result = true;
				}
			}
		}
		return result;
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
