package ss_solution;

import java.text.ParseException;
import java.util.Scanner;

/**
 * Represents a tile (or square) in the Scamble Squares Puzzle (SSP)
 * <p>
 * Each square consists of a 4-length array which represents the pictures on
 * this particular square going in clockwise direction. It is <i>always</i>
 * assumed that the first array element is the "bottom". The tile can be rotated
 * to change its orientation.
 * <p>
 * Each picture is divided into a "tail" (T) and a "head" (H).
 * 
 * @author Jared F. Bennatt
 * 
 */
public class SSPSquare {
	public static final int BOTTOM = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 2;
	public static final int LEFT = 3;

	public static final int CHECK_NOTHING = 7;
	public static final int CHECK_LEFT = 0;
	public static final int CHECK_TOP = 1;
	public static final int CHECK_RIGHT_TOP = 2;
	public static final int CHECK_RIGHT = 3;
	public static final int CHECK_BOTTOM_RIGHT = 4;
	public static final int CHECK_BOTTOM = 5;
	public static final int CHECK_LEFT_BOTTOM = 6;

	//@formatter:off
	/*
	 *        2
	 *   ------------
	 *   |          |
	 *   |          |
	 * 3 |          | 1
	 *   |          |
	 *   |          |
	 *   ------------
	 *        0
	 */
	//@formatter:on
	// represents the pictures on the square
	private final int[] pic = new int[4];

	// represents whether or not the corresponding picture is a "tail" or "head"
	// ("tails" and "heads" match up).
	private final boolean[] isHead = new boolean[4];
	private final SquareSet set;

	private SSPSquare(final SquareSet set) {
		// do nothing, just don't let anyone create a new square
		this.set = set;
	}

	/**
	 * Rotates square 90 degrees in the clockwise direction. So now the last
	 * element will become the first ("bottom") element.
	 */
	public void rotate() {
		int temp = pic[0];
		pic[0] = pic[3];

		pic[3] = pic[2];
		pic[2] = pic[1];
		pic[1] = temp; // pic[1] is set to pic[0] (from above)

		boolean bTemp = isHead[0];
		isHead[0] = isHead[3];
		isHead[3] = isHead[2];
		isHead[2] = isHead[1];
		isHead[1] = bTemp;
	}

	public boolean checkTile(int check, final SSPSquare... s) {
		switch (check) {
		case CHECK_BOTTOM:
			return checkBottom(s[0]);
		case CHECK_BOTTOM_RIGHT:
			return checkBottomRight(s[0], s[1]);
		case CHECK_LEFT:
			return checkLeft(s[0]);
		case CHECK_LEFT_BOTTOM:
			return checkLeftBottom(s[0], s[1]);
		case CHECK_RIGHT:
			return checkRight(s[0]);
		case CHECK_RIGHT_TOP:
			return checkRightTop(s[0], s[1]);
		case CHECK_TOP:
			return checkTop(s[0]);
		case CHECK_NOTHING:
			return true;
		}

		throw new RuntimeException("Incorrect check flag given: " + check);
	}

	/**
	 * 
	 * Checks whether or not this square can fit with the given right and top.
	 * The tile is rotated until it fits (if it fits). If the tile fits, at the
	 * end of this method, the tile is in the proper orientation.
	 * 
	 * @param right
	 *            square to the right (so we want to choose the left side)
	 * @param top
	 *            square to the top (so we want to choose the bottom side to
	 *            test against).
	 * @return whether or not this tile is a match (true if it is and false if
	 *         it isn't).
	 */
	public boolean checkRightTop(final SSPSquare right, final SSPSquare top) {
		final int rightPic = right.pic[LEFT];
		final int topPic = top.pic[BOTTOM];

		final boolean rightHeads = right.isHead[LEFT];
		final boolean topHeads = top.isHead[BOTTOM];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myRight = pic[RIGHT];
			final int myTop = pic[TOP];
			final boolean myRightHeads = isHead[RIGHT];
			final boolean myTopHeads = isHead[TOP];

			if (matches(myRight, myRightHeads, rightPic, rightHeads)
					&& matches(myTop, myTopHeads, topPic, topHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkBottomRight(final SSPSquare bottom,
			final SSPSquare right) {
		final int rightPic = right.pic[LEFT];
		final int bottomPic = bottom.pic[TOP];

		final boolean rightHeads = right.isHead[LEFT];
		final boolean bottomHeads = bottom.isHead[TOP];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myRight = pic[RIGHT];
			final int myBottom = pic[BOTTOM];
			final boolean myRightHeads = isHead[RIGHT];
			final boolean myBottomHeads = isHead[BOTTOM];

			if (matches(myRight, myRightHeads, rightPic, rightHeads)
					&& matches(myBottom, myBottomHeads, bottomPic, bottomHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkLeftBottom(final SSPSquare left,
			final SSPSquare bottom) {
		final int leftPic = left.pic[RIGHT];
		final int bottomPic = bottom.pic[TOP];

		final boolean leftHeads = left.isHead[RIGHT];
		final boolean bottomHeads = bottom.isHead[TOP];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myLeft = pic[LEFT];
			final int myBottom = pic[BOTTOM];
			final boolean myLeftHeads = isHead[LEFT];
			final boolean myBottomHeads = isHead[BOTTOM];

			if (matches(myLeft, myLeftHeads, leftPic, leftHeads)
					&& matches(myBottom, myBottomHeads, bottomPic, bottomHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkBottom(final SSPSquare bottom) {
		final int bottomPic = bottom.pic[TOP];

		final boolean bottomHeads = bottom.isHead[TOP];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myBottom = pic[BOTTOM];
			final boolean myBottomHeads = isHead[BOTTOM];

			if (matches(myBottom, myBottomHeads, bottomPic, bottomHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkRight(final SSPSquare right) {
		final int rightPic = right.pic[LEFT];
		final boolean rightHeads = right.isHead[LEFT];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myRight = pic[RIGHT];
			final boolean myRightHeads = isHead[RIGHT];

			if (matches(myRight, myRightHeads, rightPic, rightHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkTop(final SSPSquare top) {
		final int topPic = top.pic[BOTTOM];
		final boolean topHeads = top.isHead[BOTTOM];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myTop = pic[TOP];
			final boolean myTopHeads = isHead[TOP];

			if (matches(myTop, myTopHeads, topPic, topHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	public boolean checkLeft(final SSPSquare left) {
		final int leftPic = left.pic[RIGHT];
		final boolean leftHeads = left.isHead[RIGHT];

		// keep rotating until you find a match (4 times), otherwise there is no
		// match
		for (int i = 0; i < 4; ++i) {
			final int myLeft = pic[LEFT];
			final boolean myLeftHeads = isHead[LEFT];

			if (matches(myLeft, myLeftHeads, leftPic, leftHeads))
				return true;

			// else rotate and try again
			this.rotate();
		}

		// this tile cannot go here, no match was found
		return false;
	}

	@Override
	public String toString() {
		String string = "bottom: ";
		string += set.getString(pic[BOTTOM]);
		string += ", " + (isHead[BOTTOM] ? "H" : "T") + System.lineSeparator();

		string += "right: ";
		string += set.getString(pic[RIGHT]);
		string += ", " + (isHead[RIGHT] ? "H" : "T") + System.lineSeparator();

		string += "top: ";
		string += set.getString(pic[TOP]);
		string += ", " + (isHead[TOP] ? "H" : "T") + System.lineSeparator();

		string += "left: ";
		string += set.getString(pic[LEFT]);
		string += ", " + (isHead[LEFT] ? "H" : "T") + System.lineSeparator();

		return string;
	}

	public static boolean matches(final int p1, final boolean h1, final int p2,
			final boolean h2) {
		return p1 == p2 && h1 != h2;
	}

	/**
	 * parses a line to create a square (see text file in project for an
	 * example)
	 * 
	 * @param set
	 *            SquareSet object which maps strings (in file) to integer
	 *            values
	 * @param line
	 *            String line to parse
	 * @return a new SSPSquare object.
	 * @throws ParseException
	 */
	public static SSPSquare newSquare(final SquareSet set, final String line)
			throws ParseException {
		final Scanner scanner = new Scanner(line);

		// skip first token
		scanner.next();

		final int s1 = set.getValue(scanner.next());
		final boolean h1 = getIsHeads(scanner.next());

		final int s2 = set.getValue(scanner.next());
		final boolean h2 = getIsHeads(scanner.next());

		final int s3 = set.getValue(scanner.next());
		final boolean h3 = getIsHeads(scanner.next());

		final int s4 = set.getValue(scanner.next());
		final boolean h4 = getIsHeads(scanner.next());

		scanner.close();

		return SSPSquare.newSquare(set, s1, h1, s2, h2, s3, h3, s4, h4);
	}

	public static boolean getIsHeads(final String token) throws ParseException {
		switch (token.toLowerCase().charAt(0)) {
		case 't':
			return false;// this is tails, so not heads
		case 'h':
			return true; // this is heads, so is IS heads
		}

		throw new ParseException(token, 0);
	}

	public static SSPSquare newSquare(final SquareSet set, final int s1,
			final boolean h1, final int s2, final boolean h2, final int s3,
			final boolean h3, final int s4, final boolean h4) {
		final SSPSquare square = new SSPSquare(set);

		square.pic[0] = s1;
		square.pic[1] = s2;
		square.pic[2] = s3;
		square.pic[3] = s4;

		square.isHead[0] = h1;
		square.isHead[1] = h2;
		square.isHead[2] = h3;
		square.isHead[3] = h4;

		return square;
	}
}
