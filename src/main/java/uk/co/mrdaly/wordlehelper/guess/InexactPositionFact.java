package uk.co.mrdaly.wordlehelper.guess;

import java.util.HashSet;
import java.util.Set;

public class InexactPositionFact implements PositionFact {

    private final Set<String> inexactGuesses;

    public InexactPositionFact() {
        inexactGuesses = new HashSet<>();
    }

    @Override
    public String getRegexElement() {
        String element;
        if (inexactGuesses.isEmpty()) {
            element = ".";
        } else {
            element = buildNegativeGroup();
        }
        return element;
    }

    private String buildNegativeGroup() {
        StringBuilder sb = new StringBuilder("[^");

        for (String s : inexactGuesses) {
            sb.append(s);
        }
        sb.append("]");
        return sb.toString();
    }

    public void updateGuess(String guess) {
        inexactGuesses.add(guess);
    }
}
