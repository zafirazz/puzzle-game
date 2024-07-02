package puzzle.result;

import lombok.*;

import java.time.Duration;

/**
 * Class that takes components like name of player, whether puzzle was solved, duration of play and
 * number of moves player did.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResult {

    @NonNull private String nameOfPlayer;
    private boolean solved;
    private int numberOfMoves;
    @NonNull private String duration;

    /**
     * Retrieves the duration and formats it to Duration.
     *
     * @return duration of play
     */
    public Duration getDuration() {
        return Duration.parse(duration);
    }

    /**
     * Formats duration back to string.
     *
     * @param duration duration of a play
     */
    public void setDuration(Duration duration) {
        this.duration = duration.toString();
    }

}
