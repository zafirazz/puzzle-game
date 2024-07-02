import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;
import puzzle.model.CoinState;
import puzzle.model.Position;
import puzzle.model.Square;
import puzzle.util.BoardGameMoveSelector;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardGameMoveSelectorTest {

    private CoinState coinState;
    private BoardGameMoveSelector moveSelector;

    @BeforeEach
    public void setUp() {
        coinState = new CoinState();
        moveSelector = new BoardGameMoveSelector(coinState);
    }

    @Test
    public void testInitialState() {
        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.phaseProperty().get());
        assertFalse(moveSelector.isReadyToMove());
    }

    @Test
    public void testSelectFrom() {
        Position from = new Position(1, 1);

        assertTrue(coinState.isLegalToMoveFrom(from));
        moveSelector.selectFrom(from);
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.phaseProperty().get());
        assertFalse(moveSelector.isInvalidSelection());

        Position from2 = new Position(2, 1);
        assertTrue(coinState.isLegalToMoveFrom(from2));
        moveSelector.selectFrom(from2);
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.phaseProperty().get());
        assertFalse(moveSelector.isInvalidSelection());
    }

    @Test
    public void testSelectTo() {
        Position from = new Position(1, 1);
        Position to = new Position(3, 2);

        assertTrue(coinState.isLegalToMoveFrom(from));
        moveSelector.selectFrom(from);
        assertFalse(coinState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(from, to)));
        moveSelector.selectTo(to);
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.phaseProperty().get());
        assertTrue(moveSelector.isInvalidSelection());

        Position from1 = new Position(2, 2);
        Position to1 = new Position(2, 3);

        moveSelector.selectFrom(from1);
        assertTrue(coinState.isLegalToMoveFrom(from1));
        moveSelector.selectFrom(from);
        assertTrue(coinState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(from1, to1)));
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.phaseProperty().get());
        assertFalse(moveSelector.isInvalidSelection());
    }

    @Test
    void selectTo() {
    }

    @Test
    void makeMove() {
        Position fromValid = new Position(1, 1);
        Position toValid = new Position(0, 1);

        moveSelector.selectFrom(fromValid);
        moveSelector.selectTo(toValid);

        moveSelector.makeMove();

        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.phaseProperty().get());
        assertFalse(moveSelector.isInvalidSelection());
        assertEquals(Square.COIN, coinState.getSquare(toValid));
        assertEquals(Square.NONE, coinState.getSquare(fromValid));
    }

    @Test
    void makeMoveInvalid() {
        Position from = new Position(2, 2);
        Position to = new Position(0, 3);

        moveSelector.selectFrom(from);
        moveSelector.selectTo(to);
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.phaseProperty().get());
        assertTrue(moveSelector.isInvalidSelection());
    }

    @Test
    void reset() {
    }
}