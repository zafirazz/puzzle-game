package puzzle.result;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

public class Main {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    private static GameResult createGameResult() {
        return GameResult.builder()
                .nameOfPlayer(FAKER.name().firstName())
                .solved(FAKER.bool().bool())
                .numberOfMoves(FAKER.number().numberBetween(10, 50))
                .duration(Duration.ofSeconds(FAKER.number().numberBetween(10, 100)).toString())
                .build();
    }

    public static void main(String[] args) throws IOException {
        GameResultManager manager = new JsonGameResultManager(Path.of("/home/marbelle/uni/uni coding/homework-project-2024-zafirazz/src/main/java/puzzle/result/result.json"));
        for (var i = 0; i < 30; i++) {
            manager.add(createGameResult());
        }
        manager.getBest(10).forEach(System.out::println);
    }
}
