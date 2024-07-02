import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;
import puzzle.model.CoinState;
import puzzle.model.Position;
import puzzle.model.Square;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CoinStateTest {

    private CoinState coinState;

    @BeforeEach
    public void setUp() {
        coinState = new CoinState();
    }

    @Test
    public void testInitialState() {
        assertEquals(Square.NONE, coinState.getSquare(new Position(0, 0)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(1, 1)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(1, 2)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(2, 1)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(2, 2)));
    }

    @Test
    public void testIsOnBoard() {
        assertTrue(coinState.isOnBoard(new Position(0, 2)));
        assertTrue(coinState.isOnBoard(new Position(3, 3)));
        assertFalse(coinState.isOnBoard(new Position(4, 4)));
        assertFalse(coinState.isOnBoard(new Position(-1, 0)));
    }

    @Test
    public void testIsEmpty() throws Exception {
        Method isEmptyMethod = CoinState.class.getDeclaredMethod("isEmpty", Position.class);
        isEmptyMethod.setAccessible(true);

        assertTrue((boolean) isEmptyMethod.invoke(coinState, new Position(0, 0)));
        assertFalse((boolean) isEmptyMethod.invoke(coinState, new Position(1, 1)));
    }

    @Test
    public void testHasAdjacentCoin() throws Exception {
        Method isAdjacent = CoinState.class.getDeclaredMethod("hasAdjacentCoin", Position.class);
        isAdjacent.setAccessible(true);

        assertTrue((boolean) isAdjacent.invoke(coinState, new Position(1, 1)));
        assertFalse((boolean) isAdjacent.invoke(coinState, new Position(0, 0)));
    }



    @Test
    public void testIsLegalToMoveFrom() {
        assertTrue(coinState.isLegalToMoveFrom(new Position(1, 1)));
        assertFalse(coinState.isLegalToMoveFrom(new Position(0, 2)));
        assertFalse(coinState.isLegalToMoveFrom(new Position(3, 0)));
    }

    @Test
    public void testIsLegalMove() {
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 1), new Position(0, 1));
        assertTrue(coinState.isLegalMove(move));
        move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 1), new Position(0, 2));
        assertFalse(coinState.isLegalMove(move));
        move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(2, 1), new Position(1, 1));
        assertFalse(coinState.isLegalMove(move));
    }

    @Test
    public void makeMove() {
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 1), new Position(0, 1));
        coinState.makeMove(move);
        assertEquals(Square.NONE, coinState.getSquare(new Position(1, 1)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(0, 1)));
        move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(2, 1), new Position(3, 1));
        coinState.makeMove(move);
        assertEquals(Square.NONE, coinState.getSquare(new Position(2, 1)));
        assertEquals(Square.COIN, coinState.getSquare(new Position(3, 1)));
    }

    @Test
    public void testGetLegalMoves() {
        Set<TwoPhaseMoveState.TwoPhaseMove<Position>> move = coinState.getLegalMoves();

        assertTrue(move.contains(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 1), new Position(0, 1))));
        assertFalse(move.contains(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 1), new Position(3, 3))));
    }

    @Test
    public void testClone() {
        CoinState copy = (CoinState) coinState.clone();
        assertEquals(coinState.toString(), copy.toString());
    }

    @Test
    public void testIsSolved() {
        coinState.setSquare(new Position(0, 0), Square.COIN);
        coinState.setSquare(new Position(0, 3), Square.COIN);
        coinState.setSquare(new Position(3, 0), Square.COIN);
        coinState.setSquare(new Position(3, 3), Square.COIN);
        assertTrue(coinState.isSolved());

    }

    @Test
    public void testNotSolved() {
        coinState.setSquare(new Position(0, 0), Square.COIN);
        coinState.setSquare(new Position(0, 3), Square.NONE);
        coinState.setSquare(new Position(3, 0), Square.COIN);
        coinState.setSquare(new Position(3, 3), Square.COIN);
        assertFalse(coinState.isSolved());
    }

    @Test
    public void testEqualsMethod() {
        CoinState puzzleState = new CoinState();
        assertEquals(coinState, puzzleState);

        puzzleState.setSquare(new Position(0, 0), Square.COIN);
        assertNotEquals(coinState, puzzleState);
    }

    @Test
    public void testHashCodeMethod() {
        CoinState puzzleState = new CoinState();
        CoinState puzzleState1 = new CoinState();

        int initialHash = puzzleState.hashCode();

        assertEquals(initialHash, puzzleState.hashCode());
        assertEquals(puzzleState.hashCode(), puzzleState1.hashCode());
        puzzleState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 1), new Position(0, 1)));
        assertNotEquals(puzzleState.hashCode(), puzzleState1.hashCode());

        puzzleState.setSquare(new Position(0, 0), Square.COIN);
        assertNotEquals(coinState.hashCode(), puzzleState.hashCode());
    }

}
