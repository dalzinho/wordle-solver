package uk.co.mrdaly.wordlehelper.game;

import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.WordMatcher;
import uk.co.mrdaly.wordlehelper.ui.InputCollector;
import uk.co.mrdaly.wordlehelper.ui.Output;

import java.util.List;

public class Game {

    private final WordMatcher wordMatcher;
    private final WordListAnalyzer wordListAnalyzer;
    private final InputCollector sysInputCollector;
    private final Output sysOutput;
    private final List<String> words;


    public Game(WordMatcher wordMatcher,
                WordListAnalyzer wordListAnalyzer,
                InputCollector sysInputCollector,
                Output sysOutput,
                List<String> words) {
        this.wordMatcher = wordMatcher;
        this.wordListAnalyzer = wordListAnalyzer;
        this.sysInputCollector = sysInputCollector;
        this.sysOutput = sysOutput;
        this.words = words;
    }

    public int run(String guessFromArgs) {
        List<String> answers = words;
        int guessCount = 0;
        boolean run = true;
        String guess = guessFromArgs;

        while (run) {
            guessCount++;
            if (guess == null) {
                guess = wordListAnalyzer.getNextBestGuess(words, ".....");
            }
            sysOutput.send("Guess is: " + guess);

            final String response = sysInputCollector.collectWordleResponse(guess);

            answers = wordMatcher.getMatches(answers, guess, response);
            guess = wordListAnalyzer.getNextBestGuess(answers, ".....");


            if (answers.size() <= 1) {
                if (!answers.get(0).equals(guess)) {
                    guessCount++;
                }
                guess = answers.get(0);
                run = false;
            }
        }
        sysOutput.send("The only possible answer remaining is: " + guess + ". This would score " + guessCount);
        return guessCount;
    }
}
