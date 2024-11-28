package uk.co.mrdaly.wordlehelper.guess;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class AnswerFacts {

    private final PositionFact[] positionFacts;
    private String regexPattern;

    private final Set<String> wrongGuesses;

    private final Set<String> goodGuesses;

    @Getter
    @Setter
    private Map<String, Integer> letterAppearance;

    public AnswerFacts() {
        PositionFact[] g = new PositionFact[5];

        for (int i = 0; i < g.length; i++) {
            g[i] = new InexactPositionFact();
        }
        positionFacts = g;
        wrongGuesses = new HashSet<>();
        goodGuesses = new HashSet<>();
    }

    public void updatePositionFacts(int index, String guessedLetter, char wordleReponse) {
        if (guessIsGood(wordleReponse)) {
            if (wordleReponse == 'g') {
                positionFacts[index] = new CorrectPositionFact(guessedLetter);
            } else if (guessIsYellowOnANotYetGreenIndex(wordleReponse, index)) {
                ((InexactPositionFact) positionFacts[index]).updateGuess(guessedLetter);
            }
            goodGuesses.add(guessedLetter);
            wrongGuesses.remove(guessedLetter);
        } else {
            if (!goodGuesses.contains(guessedLetter)) {
                wrongGuesses.add(guessedLetter);
            }
        }

        regexPattern = buildRegex();
    }

    private boolean guessIsGood(char wordleResponse) {
        return wordleResponse == 'g' || wordleResponse == 'y';
    }

    private boolean guessIsYellowOnANotYetGreenIndex(char wordleResponse, int index) {
        return wordleResponse == 'y' && positionFacts[index] instanceof InexactPositionFact;
    }

    private String buildRegex() {
        return Arrays.stream(positionFacts)
                .map(PositionFact::getRegexElement)
                .collect(Collectors.joining(""));
    }

    public boolean wordMatchesRegex(String word) {
        if (regexPattern == null) {
            regexPattern = buildRegex();
        }
        return word.matches(regexPattern);
    }

    public boolean wordDoesNotContainAWrongGuess(String word) {
        return wrongGuesses.stream().noneMatch(word::contains);

    }
}
