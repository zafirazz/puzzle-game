package puzzle.solver;

import puzzle.TwoPhaseMoveState;
import puzzle.model.CoinState;
import puzzle.model.Position;

/**
 * Main class to solve the puzzle using Breadth-First Search algorithm.
 */
public class Main {
    /**
     * Starting point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
       BreadthFirstSearch<TwoPhaseMoveState.TwoPhaseMove<Position>> bfs = new BreadthFirstSearch<>();
       bfs.solveAndPrintSolution(new CoinState());
    }
}