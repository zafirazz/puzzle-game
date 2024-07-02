/**
 * Provides and contains the classes for saving the results of players in Json file.
 * <p>
 *     The main classes in this package are:
 *     <ul>
 *         <li>{@link puzzle.result.GameResult}: Represents the result of a puzzle game with player's name,
 *         whether the puzzle was solved, number of moves and duration of the game.</li>
 *         <li>{@link puzzle.result.GameResultManager}: Adds and retrieves game results. Also
 *         gets the best 10 puzzle solvers.</li>
 *         <li>{@link puzzle.result.JsonGameResultManager}: An implementation of {@link puzzle.result.GameResultManager}
 *         that stores the results of puzzle game in a JSON file.</li>
 *         <li>{@link puzzle.result.Main}: Contains a main method for testing by generating random game results and stores
 *         them using {@link puzzle.result.JsonGameResultManager}</li>
 *     </ul>
 *
 * This package encapsulates functionality for managing and processing game results
 */
package puzzle.result;