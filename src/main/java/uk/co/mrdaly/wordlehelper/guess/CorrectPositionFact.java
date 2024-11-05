package uk.co.mrdaly.wordlehelper.guess;

public class CorrectPositionFact implements PositionFact {

    private final String guess;

    public CorrectPositionFact(String guess) {
        this.guess = guess;
    }

    public String getRegexElement() {
        return guess;
    }
}
