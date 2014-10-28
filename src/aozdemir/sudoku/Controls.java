package aozdemir.sudoku;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

class Controls {
	protected static final int UP = (KeyEvent.VK_UP);
	protected static final int DOWN = (KeyEvent.VK_DOWN);
	protected static final int LEFT = (KeyEvent.VK_LEFT);
	protected static final int RIGHT = (KeyEvent.VK_RIGHT);
	protected static final int EMPTY = (KeyEvent.VK_SPACE);
	protected static final List<Integer> NUMBERS = Arrays.asList(
			new Integer[]{	KeyEvent.VK_1,
							KeyEvent.VK_2,
							KeyEvent.VK_3,
							KeyEvent.VK_4,
							KeyEvent.VK_5,
							KeyEvent.VK_6,
							KeyEvent.VK_7,
							KeyEvent.VK_8,
							KeyEvent.VK_9,});
}
