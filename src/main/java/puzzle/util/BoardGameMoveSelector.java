package puzzle.util;


import javafx.beans.property.ReadOnlyObjectProperty;
import puzzle.TwoPhaseMoveState;
import puzzle.model.CoinState;
import puzzle.model.Position;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Utility class for selecting moves in a board game.
 * Manages the state of move selection and provides methods to facilitate player moves.
 */
public class BoardGameMoveSelector {

    /**
     * Enumeration representing the phases of move selection.
     */
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE
    }

    private final CoinState model;
    private ReadOnlyObjectWrapper<Phase> phase;
    private boolean invalidSelection;
    private Position from;
    private Position to;

    /**
     * Constructs a BoardGameMoveSelector with the specified CoinState model.
     *
     * @param model Represents the state of the game board.
     */
    public BoardGameMoveSelector(CoinState model) {
        this.model = model;
        phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
        invalidSelection = false;
    }

    /**
     * Retrieves the current phase of move selection.
     *
     * @return The current phase of move selection.
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Retrieves the read-only property representing the current phase of move selection.
     *
     * @return The read-only property of the current move selection phase.
     */
    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    /**
     * Checks if the move selector is ready to execute a move.
     *
     * @return {@code true} if the move selector is ready to move, otherwise {@code false}.
     */
    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }

    /**
     * Selects a position on the game board based on the current phase of move selection.
     *
     * @param p The position to be selected.
     */
    public void select(Position p) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(p);
            case SELECT_TO -> selectTo(p);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    /**
     * Initiates the move selection from the specified position.
     *
     * @param p The position from which the move selection starts.
     */
    public void selectFrom(Position p) {
        if (model.isLegalToMoveFrom(p)) {
            from = p;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    /**
     * Completes the move selection to the specified position.
     *
     * @param p The position to which the move selection completes.
     */
    public void selectTo(Position p) {
        if (from == null) {
            throw new IllegalStateException();
        }
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(from, p);
        if (model.isLegalMove(move)) {
            to = p;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    /**
     * Retrieves the starting position of the selected move.
     *
     * @return The starting position of the selected move.
     * @throws IllegalStateException if the move selection phase is not correct.
     */
    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    /**
     * Retrieves the target position of the selected move.
     *
     * @return The target position of the selected move.
     * @throws IllegalStateException if the move selection phase is not correct.
     */
    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Checks if the current move selection is invalid.
     *
     * @return {@code true} if the current move selection is invalid, otherwise {@code false}.
     */
    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    /**
     * Executes the selected move.
     *
     * @throws IllegalStateException if the move selection phase is not correct.
     */
    public void makeMove() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }

        model.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(from, to));
        reset();
    }

    /**
     * Checks if it is legal to move from the specified position.
     *
     * @param p The position to check for legal move.
     * @return {@code true} if it is legal to move from the specified position, otherwise {@code false}.
     */
    public boolean isLegalToMoveFrom(Position p) {
        return model.getSquare(p) == model.getSquare(from);
    }

    /**
     * Checks if the specified move is legal.
     *
     * @param move The move to be checked for legality.
     * @return True if the move is legal, otherwise false.
     */
    public boolean isLegalMove(TwoPhaseMoveState.TwoPhaseMove<Position> move) {
        Position from = move.from();
        Position to = move.to();
        int steps = Math.abs(to.row() - from.row()) + Math.abs(to.col() - from.col());
        return model.isLegalMove(move);
    }

    /**
     * Resets the move selector to its initial state.
     */
    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}