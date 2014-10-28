package aozdemir.sudoku.exceptions;

public class DuplicateEntryException extends Exception{
	private static final long serialVersionUID = 9048746108890123851L;

	public DuplicateEntryException(String message) {
		super(message);
	}
}
