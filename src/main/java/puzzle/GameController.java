package puzzle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;
import puzzle.model.CoinState;
import puzzle.model.Position;
import puzzle.model.Square;
import puzzle.util.BoardGameMoveSelector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the puzzle game logic and UI.It manages the state of the board, player moves,
 * and displays game user interface using JavaFX components.
 */
public class GameController {
    private CoinState coinState;
    private BoardGameMoveSelector moveSelector;
    private int numberOfMoves = 0;
    private LocalDateTime startTime;
    private String nameOfPlayer;

    /**
     * Component for selecting the date of the play.
     */
    @FXML
    private DatePicker dateOfPlayPicker;

    /**
     * Component for displaying the start time of the game.
     */
    @FXML
    private TextField startTimeField;

    /**
     * Represents the puzzle board UI.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Text field for entering the name of the player.
     */
    @FXML
    private TextField nameOfPlayerField;

    /**
     * Counter for number of moves player made.
     */
    @FXML
    private TextField numberOfMovesField;

    /**
     * Sets initial state of the puzzle board, initializes the move selector and sets the start time.
     */
    @FXML
    public void initialize() {
        coinState = new CoinState();
        moveSelector = new BoardGameMoveSelector(coinState);
        startTime = LocalDateTime.now();
        updateStartTime();
        initBoard();
        numberOfMovesField.setText(String.valueOf(numberOfMoves));
    }

    /**
     * Initializes the puzzle board by creating squares for each position and setting up
     * their event handlers.
     */
    public void initBoard() {
        gridPane.getChildren().clear();
        for (var i = 0; i < 4; i++) {
            for (var j = 0; j < 4; j++) {
                Position position = new Position(i, j);
                createSquare(position);
            }
        }
    }

    /**
     * Creates a cell at the specified position on the board.
     *
     * @param position the position of the cell on the board.
     */
    public void createSquare(Position position) {
        StackPane square = new StackPane();
        square.setStyle("-fx-border-color: black");
        square.setPrefSize(100, 100);
        square.setOnMouseClicked(event -> handleMouseClick(position));

        if (coinState.getSquare(position) == Square.COIN) {
            createCoin(square, position);
        }
        gridPane.add(square, position.col(), position.row());
    }

    /**
     * Creates a coin at the specified position on board.
     *
     * @param square a cell where the coin will be placed.
     * @param position position of the coin on the board.
     */
    public void createCoin(StackPane square, Position position) {
        Circle coin = new Circle(20);
        coin.setFill(Color.YELLOW);
        coin.setStroke(Color.BLACK);
        coin.setOnMouseClicked(mouseEvent -> handleMouseClick(position));
        square.getChildren().add(coin);
        StackPane.setAlignment(coin, Pos.CENTER);

    }

    /**
     * Handles the mouse click event on a square of the game board.
     *
     * @param position The position of the clicked square.
     */
    private void handleMouseClick(Position position) {
        Logger.debug("Clicked on: {}", position);
        moveSelector.select(position);

        if (moveSelector.isReadyToMove()) {
            moveSelector.makeMove();
            numberOfMoves++;
            numberOfMovesField.setText(String.valueOf(numberOfMoves));
            initBoard();
            if (coinState.isSolved()) {
                Logger.info("Puzzle is solved!");
                handleSolvedAlert(true);
            } else if (coinState.isGameOver()) {
                Logger.warn("Puzzle was not solved!");
                handleGameOverAlert(true);
            }
        }
    }

    /**
     * Updates the start time field with the current start time of the game.
     */
    private void updateStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        startTimeField.setText(formatter.format(startTime));
    }

    /**
     * Displays an alert when the puzzle is solved.
     *
     * @param isSolved Indicates if the puzzle is solved.
     */
    private void handleSolvedAlert(boolean isSolved) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");

        if (isSolved) {
            alert.setTitle("Win!");
            alert.setContentText("You solved a puzzle!");
            Logger.info("Congratulations! Puzzle is solved!");
        }
        alert.showAndWait();
    }

    /**
     * Displays an alert when the game is over.
     *
     * @param isGameOver Indicates if the game is over.
     */
    private void handleGameOverAlert(boolean isGameOver) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");

        if (isGameOver) {
            alert.setTitle("You lost!");
            alert.setContentText("Sorry! You could not solve this puzzle!");
            Logger.warn("You could not solve this puzzle!");
        }
        alert.showAndWait();
    }

    @FXML
    private void dateOfPlayChanged() {
        startTime = dateOfPlayPicker.getValue().atStartOfDay();
        Logger.info("Game started at: {}", startTime);
    }

    @FXML
    private void nameOfPlayerChanged() {
        nameOfPlayer = nameOfPlayerField.getText();
        Logger.info("Player name: {}", nameOfPlayer);
    }

}
