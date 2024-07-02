package puzzle.result;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing game results to JSON file.
 */
public class JsonGameResultManager implements GameResultManager{
    private final Path filePath;
    private final Gson gson;

    /**
     * Constructs a {@link puzzle.result.JsonGameResultManager} with the specified file path.
     *
     * @param filePath The path to the JSON file where game results will be stored.
     */
    public JsonGameResultManager(@NonNull Path filePath) {
        this.filePath = filePath;
        this.gson = new Gson();
    }

    /**
     * Adds a new game results to the JSON file.
     *
     * @param result The game result to be added.
     * @return The updated list of the puzzle results after adding the new result.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    @Override
    public List<GameResult> add(@NonNull GameResult result) throws IOException {
        var results = getAll();
        results.add(result);
        try (var write = Files.newBufferedWriter(filePath)) {
            gson.toJson(results, write);
        }
        return results;
    }

    /**
     * Retrieves all game results stored in the JSON file.
     *
     * @return a list containing all game results stored in JSON file.
     * @throws IOException if an I/O error occurs while reading from the file.
     */
    @Override
    public List<GameResult> getAll() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (var read = Files.newBufferedReader(filePath)) {
            List<GameResult> results = gson.fromJson(read, new TypeToken<List<GameResult>>(){}.getType());
            return results;
        }
    }
}
