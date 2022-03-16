package uk.co.mrdaly.wordlehelper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import uk.co.mrdaly.wordlehelper.analysis.ScrabbleValueBasedTolerance;
import uk.co.mrdaly.wordlehelper.analysis.SelfPlayAnalysis;
import uk.co.mrdaly.wordlehelper.game.Game;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class WordlehelperApplication implements CommandLineRunner {

    private final Game game;
    private final SelfPlayAnalysis selfPlayAnalysis;

    private final List<String> words;
    private final ScrabbleValueBasedTolerance scrabbleValueBasedTolerance;

    private List<String> firstGuess = Arrays.asList("blurt");
    private List<String> definiteKnownIncorrect = Arrays.asList("e", "r", "t", "u", "o", "a", "s", "d", "g", "l", "v", "b");
    private List<String> knownYellows = Arrays.asList("w");
    private String greenRegex = "..i..";
//

    @Value("${calculate-mode:false}")
    private boolean calculateModeEnabled;

    public WordlehelperApplication(Game game, SelfPlayAnalysis selfPlayAnalysis, List<String> words, ScrabbleValueBasedTolerance scrabbleValueBasedTolerance) {
        this.game = game;
        this.selfPlayAnalysis = selfPlayAnalysis;

        this.words = words;
        this.scrabbleValueBasedTolerance = scrabbleValueBasedTolerance;
    }

    public static void main(String[] args) {
        SpringApplication.run(WordlehelperApplication.class, args);
    }


    @Override
    public void run(String... args) {
        if (calculateModeEnabled) {


            LocalDateTime analysisStart = LocalDateTime.now();
            log.info("started analysis at " + analysisStart);

            Map<String, Double> map = selfPlayAnalysis.buildBestFirstGuessMap();
            try (FileWriter fw = new FileWriter("best_guesses")) {
                final List<String> bestGuesses = map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .map(entry -> entry.getKey() + " -> " + entry.getValue())
                        .collect(Collectors.toList());
                for (String guess : bestGuesses) {
                    fw.write(guess + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            LocalDateTime analysisEnd = LocalDateTime.now();
            log.info("ended analysis at " + analysisEnd);
            log.info("Duration: " + Duration.between(analysisStart, analysisEnd));
        }

        String guess;

        Random random = new Random();

        if (firstGuess != null) {
            int indexOfFirst = random.nextInt(firstGuess.size());
            guess = firstGuess.get(indexOfFirst);
        } else {
            final String regexForTolerance = scrabbleValueBasedTolerance.getRegexForTolerance(3);


            List<String> firstGuessList = words.stream()
                    .filter(word -> word.matches(regexForTolerance))
                    .filter(this::wordHasFiveDistinctCharacters)
                    .collect(Collectors.toList());
            int indexOfFirst = random.nextInt(firstGuessList.size());
            guess = firstGuessList.get(indexOfFirst);
        }
        game.run(guess);
    }

    private boolean wordHasFiveDistinctCharacters(String word) {
        return Arrays.stream(word.split(""))
                .distinct().count() == 5;
    }
}
