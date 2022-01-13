package uk.co.mrdaly.wordlehelper.guess;

public class CorrectGuess implements Guess {

    private final String guess;

    public CorrectGuess(String guess) {
        this.guess = guess;
    }

    public String getRegexElement() {
        return guess;
    }
}
