package ss_solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Set of Scramble Squares (for now it's assumed this is a 9X9 puzzle)
 * <p>
 * Holds information about how to label each picture and each square of the
 * puzzle.
 * 
 * @author Jared F. Bennatt
 * 
 */
public class SquareSet {
	/*
	 * This algorithm checks in a very specific way. It starts in the center
	 * then snakes around going right, then down, then around the square:
	 */
	// @formatter:off
	/*
	 * ---------------------
	 * |      |     |      |
	 * |  6   |  7  |  8   |
	 * |      |     |      |
	 * ---------------------
	 * |      |     |      |
	 * |  5   |  0  |  1   |
	 * |      |     |      |
	 * ---------------------
	 * |      |     |      |
	 * |  4   |  3  |  2   |
	 * |      |     |      |
	 * ---------------------
	 */
	 // @formatter:on

	// index that specifies which check you need to do for the current tile
	private static final int[] CHECK_ARRAY = { SSPSquare.CHECK_NOTHING,
			SSPSquare.CHECK_LEFT, SSPSquare.CHECK_TOP,
			SSPSquare.CHECK_RIGHT_TOP, SSPSquare.CHECK_RIGHT,
			SSPSquare.CHECK_BOTTOM_RIGHT, SSPSquare.CHECK_BOTTOM,
			SSPSquare.CHECK_LEFT_BOTTOM, SSPSquare.CHECK_LEFT_BOTTOM };

	// specifies which tiles (in the partially constructed solution) that you
	// should check with each step in the above array
	private static final int[][] CHECK_INDEXES = { {}, { 0 }, { 1 }, { 2, 0 },
			{ 3 }, { 4, 0 }, { 5 }, { 6, 0 }, { 7, 1 } };

	private final Map<String, Integer> valueMap = new HashMap<String, Integer>();
	private final Map<Integer, String> stringMap = new HashMap<Integer, String>();
	private final SSPSquare tile[] = new SSPSquare[9];

	public SquareSet(final String file)
			throws FileNotFoundException, ParseException {
		final Scanner scanner = new Scanner(new File(file));

		// skip to the line where the first token is "Pictures"
		String line;
		while (!(line = scanner.nextLine()).toLowerCase().contains("picture"))
			;

		// scan line to assign values to the value map
		setMaps(line);

		// scan until you see the first tile (which should start with '1'):
		while (true) {
			line = scanner.nextLine();
			if (line.length() > 0 && line.charAt(0) == '1')
				break; // break out of loop
			// else continue loop
		}

		// now line holds the first line of tile information. Keep going until
		// the next line is null

		int index = 0;
		do {
			tile[index++] = SSPSquare.newSquare(this, line);
			if (scanner.hasNextLine())
				line = scanner.nextLine();
			else
				break;
		} while (true);

		scanner.close();
	}

	public boolean solve() {
		final Stack<SSPSquare> availableSquares = new Stack<SSPSquare>();

		for (int i = 0; i < tile.length; ++i) {
			availableSquares.push(tile[i]);
		}

		return solve(0, availableSquares);
	}

	public boolean solve(final int step, final Stack<SSPSquare> avail) {
		// this is what will be sent to the next step
		final Stack<SSPSquare> newAvail = new Stack<SSPSquare>();
		final Stack<SSPSquare> used = new Stack<SSPSquare>();

		final int nextStep = step + 1;
		final int test = CHECK_ARRAY[step];

		final SSPSquare checkSpace[] = new SSPSquare[CHECK_INDEXES[step].length];
		for (int i = 0; i < checkSpace.length; ++i)
			checkSpace[i] = tile[CHECK_INDEXES[step][i]];

		// try to find a match for this step from the available squares
		while (true) {
			// check next available. If it matches, then transfer the rest of
			// the available to the newAvailable then go on to the next step

			// is the available stack empty? If so, there is no solution, return
			// false. This is a base case
			if (avail.isEmpty())
				return false;

			// 1.) place tile in current position (within internal array)
			tile[step] = avail.pop();

			// 2.) check tile
			if (tile[step].checkTile(test, checkSpace)) {
				// this tile can go here. Fill the newAvail with the rest of
				// this avail stack AND all of the used stack
				for (final SSPSquare tile : avail)
					newAvail.push(tile);

				for (final SSPSquare tile : used)
					newAvail.push(tile);

				// try to go to the next step (if there is one). If a solution
				// can be found, then return true, otherwise keep going.
				if (nextStep >= CHECK_ARRAY.length || solve(nextStep, newAvail))
					return true;

				// else we need to empty newAvail
				newAvail.clear();
			}

			// otherwise, this tile couldn't go here. Either because it doesn't
			// fit OR because it causes a problem later. Either way, add it to
			// the used stack

			used.push(tile[step]);
		}
	}

	private void setMaps(final String line) {
		final Scanner scanner = new Scanner(line);

		// skip first token
		scanner.next();

		int val = 0;

		while (scanner.hasNext()) {
			final String name = getName(scanner.next());
			setPair(name, val++);
		}

		scanner.close();
	}

	/**
	 * Just cuts off the tailing comma (if it's there).
	 * 
	 * @param token
	 * @return
	 */
	private static String getName(String token) {
		token = token.trim();
		if (token.charAt(token.length() - 1) == ',')
			token = token.substring(0, token.length() - 1);

		return token;
	}

	private void setPair(final String string, final int val) {
		valueMap.put(string, val);
		stringMap.put(val, string);
	}

	public String getString(final int val) {
		return stringMap.get(val);
	}

	public int getValue(final String val) {
		return valueMap.get(val);
	}

	@Override
	public String toString() {
		String string = "";
		for (final SSPSquare tile : this.tile) {
			string += tile + System.lineSeparator();
		}

		return string;
	}

	// public static final String TEST_FILE = "New Puzzle";
	// public static final String TEST_FILE = "Lion Tiger Panther Leopard";
	public static final String TEST_FILE = "Crazy Planes";

	public static void main(String... args)
			throws FileNotFoundException, ParseException {
		final SquareSet set = new SquareSet(TEST_FILE);

		if (set.solve())
			System.out.println(set);
		else
			System.out.println("no solution found");
	}
}
