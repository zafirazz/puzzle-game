package puzzle.model;

/**
 * Represents a position of the coin on a puzzle board.
 *
 * @param row represents a row index
 * @param col represents a column index
 */
public record Position(int row, int col) {
    /**
     * Returns a string representation of the coin position.
     *
     * @return a position in format: "Move: (row, column)"
     */
    @Override
    public String toString() {
        return String.format("Move: (%d, %d)", row, col);
    }
}
