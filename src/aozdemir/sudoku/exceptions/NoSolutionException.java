package aozdemir.sudoku.exceptions;

public class NoSolutionException extends Exception{
	private static final long serialVersionUID = -6678547514800919339L;

	public NoSolutionException(String message) {
		super(message);
	}
}
