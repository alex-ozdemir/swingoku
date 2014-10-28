package aozdemir.sudoku.util;

import java.util.LinkedList;

public class Guess {
	private int r;
	private int c;
	private LinkedList<Character> options;
	
	public Guess(int r, int c, LinkedList<Character> options) {
		this.r = r;
		this.c = c;
		this.options = options;
	}

	public Character popOption() {
		return options.pop();
	}
	
	public boolean hasOptions() {
		return !options.isEmpty();
	}
	
	public int getR() {
		return r;
	}
	
	public int getC() {
		return c;
	}
	
	public String toString() {
		return String.format("<Cell (%d,%d), Options %s>", r, c, options.toString());
	}
}
