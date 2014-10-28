package aozdemir.sudoku;

import java.util.Stack;
import java.util.LinkedList;

import aozdemir.sudoku.exceptions.DuplicateEntryException;
import aozdemir.sudoku.exceptions.NoSolutionException;
import aozdemir.sudoku.util.Guess;

public class SolveBoard extends ColorBoard{
	private static final long serialVersionUID = 1L;
	Stack<char[][]> savedStates;
	Stack<Guess> savedGuesses;
	
	public SolveBoard() {
		super();
	}
	
	public SolveBoard(char[][] contents) {
		this();
		applyContents(contents);
	}
	
	public void solve() throws NoSolutionException {
		savedStates = new Stack<char[][]>();
		savedGuesses = new Stack<Guess>();
		while (true) {
			try{
				infer();
				if (!filled()) {
					guess();
				} else {
					handleColors();
					return;
				}
			} catch (DuplicateEntryException e) {
				if (savedStates.isEmpty())
					throw new NoSolutionException("The board could not be solved");
				reGuess();
			}
		}
	}

	private void guess() {
		savedStates.push(getContents());
		Guess g = getGuess();
		Character c = g.popOption();
		savedGuesses.push(g);
		this.select(g.getR(), g.getC());
		this.enterCharacter(c);
	}

	private void reGuess() {
		char[][] state = savedStates.pop();
		Guess g = savedGuesses.pop();
		Character c = g.popOption();
		applyContents(state);
		this.select(g.getR(), g.getC());
		this.enterCharacter(c);
		if (g.hasOptions()) {
			savedGuesses.push(g);
			savedStates.push(state);
		}
	}
	
	private char[][] getContents() {
		char[][] contents = new char[height][width];
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++)
				contents[r][c] = cells[r][c].content;
		return contents;
	}
	
	private void applyContents(char[][] contents) {
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++)
				cells[r][c].setContent(contents[r][c]);
	}
	
	private Guess getGuess() {
		setupPossibleEntries();
		for (int r = 0; r < height; r++)
			for (int c = 0; c < height; c++)
				if (cells[r][c].content == ' ') {
					LinkedList<Character> options = new LinkedList<Character>();
					for (char option : possibleEntries)
						options.add(option);
					return new Guess(r, c, options);

				}
		return null;
	}
	
	public void infer() throws DuplicateEntryException {
		boolean newInformation = true;
		while (newInformation) {
			newInformation = false;
			newInformation |= inferRows();
			newInformation |= inferColumns();
			newInformation |= inferBoxes();
			newInformation |= inferCells();
		}
	}


	private boolean inferRows() throws DuplicateEntryException {
		boolean newInformation = false;
		for (int r = 0; r < height; r++)
			newInformation |= inferRow(r);
		return newInformation;
	}
	
	private boolean inferRow(int r) throws DuplicateEntryException {
		setupPossibleEntries();
		int lastBlank = -1;
		for(int c = 0; c < width; c++) {
			if (cells[r][c].content != ' ')			
				if (possibleEntries.contains(cells[r][c].content)) {
					possibleEntries.remove(new Character(cells[r][c].content));
				} else {
					throw new DuplicateEntryException(
							String.format("Duplicate character %c in row %d",
									cells[r][c].content, r));
				}
			else
				lastBlank = c;
		}
		if (possibleEntries.size() == 1 && lastBlank > -1) {
			cells[r][lastBlank].setContent(possibleEntries.get(0));
			return true;
		}
		return false;
	}


	private boolean inferColumns() throws DuplicateEntryException {
		boolean newInformation = false;
		for (int c = 0; c < width; c++)
			newInformation |= inferColumn(c);
		return newInformation;
	}

	private boolean inferColumn(int c) throws DuplicateEntryException {
		setupPossibleEntries();
		int lastBlank = -1;
		for(int r = 0; r < width; r++) {
			if (cells[r][c].content != ' ')			
				if (possibleEntries.contains(cells[r][c].content)) {
					possibleEntries.remove(new Character(cells[r][c].content));
				} else {
					throw new DuplicateEntryException(
							String.format("Duplicate character %c in column %d",
									cells[r][c].content, c));
				}
			else
				lastBlank = r;
		}
		if (possibleEntries.size() == 1 && lastBlank > -1) {
			cells[lastBlank][c].setContent(possibleEntries.get(0));
			return true;
		}
		return false;
	}
	
	private boolean inferBoxes() throws DuplicateEntryException {
		boolean newInformation = false;
		for (int c = 0; c < numberOfBoxes; c++)
			newInformation |= inferBox(c);
		return newInformation;		
	}
	
	private boolean inferBox(int b) throws DuplicateEntryException {
		setupPossibleEntries();
		int lastBlank = -1;
		Cell[] box = boxes[b];
		for (int i = 0; i < box.length; i++) {
			if (box[i].content != ' ')			
				if (possibleEntries.contains(box[i].content)) {
					possibleEntries.remove(new Character(box[i].content));
				} else {
					throw new DuplicateEntryException(
							String.format("Duplicate character %c in box %d",
									box[i].content, i));
				}
			else
				lastBlank = i;
		}
		if (possibleEntries.size() == 1 && lastBlank > -1) {
			box[lastBlank].setContent(possibleEntries.get(0));
			return true;
		}
		return false;	
	}
	
	private boolean inferCells() {
		boolean newInformation = false;
		for (Cell cell : this)
			newInformation |= inferCell(cell);
		return newInformation;
	}
	
	private boolean inferCell(Cell cell) {
		boolean newInformation = false;
		if (cell.content == ' ') {
			int r = cell.row;
			int c = cell.column;
			int b = cell.getBoxNumber();
			setupPossibleEntries();
			for (Cell other : getRow(r))
				if (possibleEntries.contains(other.content))
					possibleEntries.remove(new Character(other.content));
			for (Cell other : getColumn(c))
				if (possibleEntries.contains(other.content))
					possibleEntries.remove(new Character(other.content));
			for (Cell other : getBox(b))
				if (possibleEntries.contains(other.content))
					possibleEntries.remove(new Character(other.content));
			if (possibleEntries.size() == 1) {
				newInformation = true;
				cell.setContent(possibleEntries.pop());
			}
		}
		return newInformation;
		
	}
}
