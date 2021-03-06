package part1.priorityQueues;

import edu.princeton.cs.algs4.Queue;

import static java.lang.Math.abs;

public class Board {

    private char[] jBoard;
    private int jBoardDimension;

    // construct a board from an n-by-n array of blocks
    //(where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException();
        }

        jBoardDimension = blocks.length;
        jBoard = new char[jBoardDimension * jBoardDimension];
        int elem = 0;
        for (int[] cs : blocks) {
            for (int c : cs) {
                jBoard[elem++] = (char) c;
            }
        }

    }

    // board dimension n
    public int dimension() {
        return jBoardDimension;
    }

    // number of blocks out of place
    public int hamming() {
        int out = 0;
        int i = 1;
        // from 1 to end
        for (char c : jBoard) {
            if ((int) c != 0 && (int) c != i) {
                // check last properly
                if ((int) c == 0 && (int) c == jBoard.length - 1) {
                    continue;
                }
                out++;
            }
            i++;
        }
        return out;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int out = 0;
        int i = 1;
        // from 1 to end
        for (char c : jBoard) {
            if ((int) c != 0 && (int) c != i) {
                // check last properly
                if ((int) c == 0 && (int) c == jBoard.length - 1) {
                    continue;
                }
                // row steps
                final int dR = toXY((int) c)[0] - toXY(i)[0];
                // colomn
                final int dC = toXY((int) c)[1] - toXY(i)[1];
                // count all
                out += abs(dR) + abs(dC);
            }
            i++;
        }
        return out;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        return this.copyButTwin(true);
    }

    // return the clone of current board on "false" input,
    // and twin-copy if input param is "true"
    private Board copyButTwin(boolean twi) {
        boolean twined = false;
        // clone 1D array into 2D
        int[][] twinBoard = new int[jBoardDimension][jBoardDimension];
        int jIndex = 0;
        int iOne = 0, iTwo = 0;
        for (int i = 0; i < jBoardDimension; i++) {
            for (int k = 0; k < jBoardDimension; k++) {
                twinBoard[i][k] = this.jBoard[jIndex++];
                // find first two tiles (not DIRKA) and remember to swap
                if (!twined && jIndex > 0 && (int) this.jBoard[jIndex - 1] != 0
                        && (int) this.jBoard[jIndex] != 0) {
                    iOne = jIndex;
                    iTwo = jIndex - 1;
                    twined = true;
                }
            }
        }

        Board twin = new Board(twinBoard);
        if (twi) {
            twin.swapTiles(iOne, iTwo);
        }
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension())
            return false;
        for (int i = 0; i < that.dimension() * that.dimension(); i++) {
            if (this.jBoard[i] != that.jBoard[i])
                return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> jNeighbors = new Queue<>();
        int currentDIRKAIndex = -1;
        // try to find dirka
        for (int i = 0; i < jBoard.length; i++) {
            if (jBoard[i] == 0) {
                currentDIRKAIndex = i;
                break;
            }
        }
        int n = currentDIRKAIndex / jBoardDimension;
        int m = currentDIRKAIndex % jBoardDimension;
        // dirka moves UP
        if (n - 1 >= 0) {
            Board u = this.copyButTwin(false);
            u.swapTiles(currentDIRKAIndex, xyTo1D(n - 1, m));
            jNeighbors.enqueue(u);
        }
        // dirka moves down
        if (n + 1 < jBoardDimension) {
            Board d = this.copyButTwin(false);
            d.swapTiles(currentDIRKAIndex, xyTo1D(n + 1, m));
            jNeighbors.enqueue(d);
        }
        // dirka moves left
        if (m - 1 >= 0) {
            Board l = this.copyButTwin(false);
            l.swapTiles(currentDIRKAIndex, xyTo1D(n, m - 1));
            jNeighbors.enqueue(l);
        }
        // dirka moves right
        if (m + 1 < jBoardDimension) {
            Board r = this.copyButTwin(false);
            r.swapTiles(currentDIRKAIndex, xyTo1D(n, m + 1));
            jNeighbors.enqueue(r);
        }
        return jNeighbors;
    }

    // returns [position] in 1D array from [X][Y] position of 2D array
    private int xyTo1D(final int x, final int y) {
        return (jBoardDimension * (x) + y);
    }

    // returns {X,Y} coords from [1D] coordinate.
    // init array count begins from 1 !!!
    private int[] toXY(final int d) {
        int x = (d - 1) / jBoardDimension;
        int y = (d - 1) % jBoardDimension;
        return new int[] { x, y };
    }

    // swap tiles i and k of this.jBoard
    private void swapTiles(int i, int k) {
        char aux = this.jBoard[i];
        this.jBoard[i] = this.jBoard[k];
        this.jBoard[k] = aux;
    }


    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(jBoardDimension + "\n");
        int lol = 0;
        for (int i = 0; i < jBoard.length; i++) {
            sb.append(String.format("%2d ", (int) jBoard[i]));
            // alws make new line followed by matrix dimension
            if (lol++ % jBoardDimension == jBoardDimension - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
    }

}
