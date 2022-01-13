package uk.co.mrdaly.wordlehelper.guess;

import java.util.HashSet;
import java.util.Set;

public class InexactGuess implements Guess {

    private final Set<String> inexactGuesses;

    public InexactGuess() {
        inexactGuesses = new HashSet<>();
    }

    @Override
    public String getRegexElement() {
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
