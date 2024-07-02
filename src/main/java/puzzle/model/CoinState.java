package puzzle.model;

import puzzle.TwoPhaseMoveState;

import java.util.*;

import org.tinylog.Logger;

/**
 * Represents the state of the coin on the puzzle board.
 * Implements the rules of the puzzle game.
 */
public class CoinState implements TwoPhaseMoveState<Position> {

    private static final int BOARD_SIZE = 4;

    private final Square[][] board = new Square[BOARD_SIZE][BOARD_SIZE];

    private Map<CoinState, TwoPhaseMove<Position>> previousMoves = new HashMap<>();

    /**
     * Initializes the puzzle board with coins in the center.
     */
    public CoinState() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if ((i == 1 || i == 2) && (j == 1 || j == 2)) {
                    board[i][j] = Square.COIN;
                } else {
                    board[i][j] = Square.NONE;
                }
            }
        }
        Logger.info("Board initialized.");
    }

    /**
     * Retreives the square at the specified position.
     *
     * @param p position on the board.
     * @return square at the specified position.
     */
    public Square getSquare(Position p) {
        return board[p.row()][p.col()];
    }

    /**
     * Sets the square at the specified position.
     *
     * @param p position on the board.
     * @param square cell of the board at the specified position.
     */
    public void setSquare(Position p, Square square) {
        board[p.row()][p.col()] = square;
        Logger.debug("Board updated from " + p + " to " + square);
    }

    /**
     * Checks if the specified position is empty or not.
     *
     * @param p position on the board.
     * @return {@code true} if position is empty, otherwise returns {@code false}.
     */
    public boolean isEmpty(Position p) {
        return getSquare(p) == Square.NONE;
    }

    /**
     * Checks if a coin position is adjacent or not.
     *
     * @param p position on the board.
     * @return {@code true} if the position is adjacent coin, {@code false} otherwise.
     */
    private boolean hasAdjacentCoin(Position p) {
        boolean isAdjacent = (isOnBoard(new Position(p.row() + 1, p.col())) && getSquare(new Position(p.row() + 1, p.col())) == Square.COIN) ||
                (isOnBoard(new Position(p.row() - 1, p.col())) && getSquare(new Position(p.row() - 1, p.col())) == Square.COIN) ||
                (isOnBoard(new Position(p.row(), p.col() + 1)) && getSquare(new Position(p.row(), p.col() + 1)) == Square.COIN) ||
                (isOnBoard(new Position(p.row(), p.col() - 1)) && getSquare(new Position(p.row(), p.col() - 1)) == Square.COIN);
        Logger.debug("Coin {} has adjacent coin: {}", p, isAdjacent);
        return isAdjacent;
    }

    /**
     * Checks if position is on the board or not.
     *
     * @param position the position of coin on the board.
     * @return {@code true} if a coin is on the board, {@code false} otherwise.
     */
    public boolean isOnBoard(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE &&
                position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    /**
     * Returns a string representation of the current puzzle board state.
     *
     * @return a representation of the current board state.
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append('\n');
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Checks if the specified position is legal to move to another cell.
     *
     * @param position the position of coin on the puzzle board.
     * @return {@code true} if position is not empty and is adjacent coin, {@code false} otherwise.
     */
    @Override
    public boolean isLegalToMoveFrom(Position position) {
        boolean isLegal = isOnBoard(position) && !isEmpty(position) && hasAdjacentCoin(position);
        Logger.debug("It is legal to move from {}", position);
        return isLegal;
    }

    /**
     * Checks if puzzle was solved.
     *
     * @return {@code true} if the position of all coins are in the corners of the puzzle board, {@code false} otherwise.
     */
    @Override
    public boolean isSolved() {
        boolean solution = getSquare(new Position(0, 0)) == Square.COIN &&
                getSquare(new Position(0, 3)) == Square.COIN &&
                getSquare(new Position(3, 0)) == Square.COIN &&
                getSquare(new Position(3, 3)) == Square.COIN;
        Logger.info("Puzzle is solved: {}", solution);
        return solution;
    }

    /**
     * Checks if the move is legal with specified positions of coins.
     *
     * @param positionTwoPhaseMove moves to check (from one cell, to another cell).
     * @return {@code true} if two moves are legal, {@code false} otherwise.
     */
    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        Position from = positionTwoPhaseMove.from();
        Position to = positionTwoPhaseMove.to();

        if (!isOnBoard(from) || !isOnBoard(to) || isEmpty(from) || !isEmpty(to) || !hasAdjacentCoin(from)) {
            Logger.warn("Move {} is illegal", positionTwoPhaseMove);
            return false;
        }

        int rowDifference = Math.abs(from.row() - to.row());
        int colDifference = Math.abs(from.col() - to.col());

        if ((rowDifference == 0 && colDifference >= 1 && colDifference <= 3) || (colDifference == 0 && rowDifference >= 1 && rowDifference <= 3)) {
            int rowStep = Integer.compare(to.row(), from.row());
            int colStep = Integer.compare(to.col(), from.col());

            for (int i = 1; i < Math.max(rowDifference, colDifference); i++) {
                int row = from.row() + i * rowStep;
                int col = from.col() + i * colStep;
                if (!isEmpty(new Position(row, col))) {
                    Logger.warn("This move is illegal: {}", positionTwoPhaseMove);
                    return false;
                }
            }
            Logger.debug("Move is legal: {}", positionTwoPhaseMove);
            return true;
        }

        Logger.warn("Move is invalid: {}", positionTwoPhaseMove);
        return false;
    }


    /**
     * Checks if puzzle was not solved.
     *
     * @return {@code true} if there were no legal moves left for each position, {@code false} otherwise.
     */
    public boolean isGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position from = new Position(i, j);
                if (isLegalToMoveFrom(from)) {
                    Logger.warn("Move {} is illegal. You did not solve puzzle", from);
                    return false;
                }
            }
        }
        Logger.info("Game is over");
        return true;
    }

    /**
     * Makes a move with specified positions.
     *
     * @param positionTwoPhaseMove move to execute.
     */
    @Override
    public void makeMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        Position from = positionTwoPhaseMove.from();
        Position to = positionTwoPhaseMove.to();
        if (isLegalMove(positionTwoPhaseMove)) {
            setSquare(to, Square.COIN);
            setSquare(from, Square.NONE);
            previousMoves.put((CoinState) this.clone(), positionTwoPhaseMove);
            Logger.info("Made move from {} to {}", from, to);
        }
    }

    /**
     * Retrieves the set of possible legal moves.
     *
     * @return set of legal moves.
     */
    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        Set<TwoPhaseMove<Position>> legalMoves = new HashSet<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position from = new Position(i, j);
                if (isLegalToMoveFrom(from)) {
                    addMovesToSet(legalMoves, from, 0, 1);
                    addMovesToSet(legalMoves, from, 1, 0);
                    addMovesToSet(legalMoves, from, -1, 0);
                    addMovesToSet(legalMoves, from, 0, -1);
                }
            }
        }
        return legalMoves;
    }

    /**
     * Adds possible legal moves to the set from specified position with given increments.
     *
     * @param legalMoves set of legal moves.
     * @param from position from which move will be executed.
     * @param rowIncr the row increment for the move.
     * @param colIncr the column increment for the move.
     */
    private void addMovesToSet(Set<TwoPhaseMove<Position>> legalMoves, Position from, int rowIncr, int colIncr) {
        for (int i = 1; i < 4; i++) {
            Position to = new Position(from.row() + rowIncr * i, from.col() + colIncr * i);
            if (isOnBoard(to) && isLegalMove(new TwoPhaseMove<>(from, to))) {
                legalMoves.add(new TwoPhaseMove<>(from, to));
            }
        }
    }

    /**
     * Checks if the state of coin is equal to another object.
     * Two state of coin object are equal if their board states are identical.
     *
     * @param o the object to compare with this coin state.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoinState other)) return false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[i][j].equals(other.board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a hash code based on the hash codes of the individual
     * squares in the board.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * Creates and returns a copy of coin state.
     *
     * @return new object that is a deep copy of this instance.
     */
    @Override
    public TwoPhaseMoveState<Position> clone() {
        CoinState clone = new CoinState();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                clone.setSquare(new Position(i, j), getSquare(new Position(i, j)));
            }
        }
        return clone;
    }

    /**
     * Main method for testing.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        var coinState = new CoinState();
        Logger.info("Initial puzzle board:\n{}", coinState);
    }
}