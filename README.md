# Scramble-Squares-Puzzle-Solver
Program that using a backtracking algorithm to solve a [Scramble Squares® Puzzle](https://www.scramblesquares.com/).

# Disclaimer
This is *not* my algorithm and I *do not* claim to have created it. This is *my implementation* of the algorithm presented in [USING BACKTRACKING TO SOLVE THE
SCRAMBLE SQUARES® PUZZLE](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/backtrackingPaper.pdf).

## Usage
The main method is located in [src/ss_solution/SquareSet.java](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/src/ss_solution/SSPSquare.java) (at the very bottom, `line 214`). The input for this program is a text file. There are two examples given: [Crazy Planes](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/Crazy%20Planes) and [Lion Tiger Panther Leopard](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/Lion%20Tiger%20Panther%20Leopard). Each side should be represented by a letter which is given at the top of the file (below the title--which is skipped in parsing), then each line after that represents a square.

### Squares
Each picture has a "head" (H) and a "tail" (T) (which is which doesn't really matter as long as you're consistent). When specifying a square the order should be given in counterclockwise order. There is a comment at the top of [SSPSquare.java](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/src/ss_solution/SSPSquare.java) which shows, visually, why the program expects conterclockwise order (note that the orientation isn't important--just that counterclockwise order).

## Output
The output isn't the easiest to read. It's given in order of how it was solved, so center, right-center, bottom-right, then clockwise around the puzzle; there is a visual comment at the top of [SquareSet.java](https://github.com/jbennatt/Scramble-Squares-Puzzle-Solver/blob/master/src/ss_solution/SquareSet.java) that shows the array indexes for the tiles--this is the order it will be printed. You'd have to figure out which each tile is based on what's output (there shouldn't be duplicates so given the right-left-top-bottom should uniquely identify a square.

```
bottom: B, H
right: G, T
top: Y, T
left: W, H

bottom: B, H
right: Y, T
top: W, T
left: G, H

bottom: B, H
right: W, T
top: B, T
left: Y, H

bottom: G, H
right: Y, T
top: B, T
left: W, H

bottom: B, H
right: W, T
top: G, T
left: Y, H

bottom: G, H
right: W, T
top: Y, T
left: B, H

bottom: Y, H
right: B, H
top: W, T
left: G, T

bottom: Y, H
right: G, H
top: W, T
left: B, T

bottom: W, H
right: G, H
top: Y, T
left: G, T
```
