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


    public Game(WordMatcher wordMatcher,
                WordListAnalyzer wordListAnalyzer,
                InputCollector sysInputCollector,
                Output sysOutput) {
        this.wordMatcher = wordMatcher;
        this.wordListAnalyzer = wordListAnalyzer;
        this.sysInputCollector = sysInputCollector;
        this.sysOutput = sysOutput;
    }

    public int run(String calculatedGuess, List<String> possibleAnswers, int iteration) {
        if (possibleAnswers.size() == 1) {
            sysOutput.send("The only possible answer remaining is: " + possibleAnswers.get(0) + ". This would score " + iteration);
            return iteration;
        }

        logRemainingWords(possibleAnswers);
        String guess = collectNextGuess(calculatedGuess);
        final String response = collectUserResponse(guess);

        if (response.equals("ggggg")) {
            sysOutput.send("nice one! You got the answer, " + guess + ", in " + iteration + " goes.");
            return iteration;
        }

        List<String> remainingAnswers = wordMatcher.getMatches(possibleAnswers, guess, response);
        String nextGuess = wordListAnalyzer.getNextBestGuess(remainingAnswers, ".....");
        return run(nextGuess, remainingAnswers, iteration + 1);
    }

    private String collectUserResponse(String guess) {
        sysOutput.send("Guess is: " + guess);
        final String response = sysInputCollector.collectWordleResponse(guess);
        return response;
    }

    private void logRemainingWords(List<String> possibleAnswers) {
        if (possibleAnswers.size() > 5) {
            sysOutput.send(possibleAnswers.size() + " possibilities remain!");
        } else {
            sysOutput.send("remaining words are: " + String.join(", ", possibleAnswers));
        }
    }

    private String collectNextGuess(String calculatedGuess) {
        sysOutput.send("The suggested guess is " + calculatedGuess);
        sysOutput.send("Hit return to go with that, otherwise type your own suggestion");
        return sysInputCollector.collectGuess(calculatedGuess);

    }
}
