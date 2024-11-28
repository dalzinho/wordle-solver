package uk.co.mrdaly.wordlehelper.game;

import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.guess.AnswerFacts;
import uk.co.mrdaly.wordlehelper.service.AnswerFactsRefresher;
import uk.co.mrdaly.wordlehelper.ui.InputCollector;
import uk.co.mrdaly.wordlehelper.ui.Output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private final AnswerFactsRefresher wordMatcher;
    private final WordListAnalyzer wordListAnalyzer;
    private final InputCollector sysInputCollector;
    private final Output sysOutput;


    public Game(AnswerFactsRefresher wordMatcher,
                WordListAnalyzer wordListAnalyzer,
                InputCollector sysInputCollector,
                Output sysOutput) {
        this.wordMatcher = wordMatcher;
        this.wordListAnalyzer = wordListAnalyzer;
        this.sysInputCollector = sysInputCollector;
        this.sysOutput = sysOutput;
    }

    public int run(String calculatedGuess, AnswerFacts answerFacts, List<String> possibleAnswers, int iteration) {
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

        AnswerFacts matches = wordMatcher.getMatches(answerFacts, guess, response);
        List<String> remainingAnswers = filter(possibleAnswers, matches);
        String nextGuess = wordListAnalyzer.getNextBestGuess(remainingAnswers, ".....");

        return run(nextGuess, matches, remainingAnswers, iteration + 1);
    }

    private List<String> filter(List<String> words, AnswerFacts answerFacts) {
        return words.stream()
                .filter(answerFacts::wordDoesNotContainAWrongGuess)
                .filter(answerFacts::wordMatchesRegex)
                .filter(word -> containsKnownInexactGuesses(word, answerFacts.getLetterAppearance()))
                .toList();
    }

    private boolean containsKnownInexactGuesses(String word, Map<String, Integer> seenLetters) {
        Map<String, Integer> count = new HashMap<>();
        for (String s : word.split("")) {
            count.merge(s, 1, Integer::sum);
        }

        return seenLetters.entrySet().stream()
                .allMatch(entry -> count.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    private String collectUserResponse(String guess) {
        sysOutput.send("Guess is: " + guess);
        final String response = sysInputCollector.collectWordleResponse(guess);
        return response;
    }

    private void logRemainingWords(List<String> possibleAnswers) {
        if (possibleAnswers.size() > 20) {
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
