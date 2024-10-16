package uk.co.mrdaly.wordlehelper.ui;

public interface InputCollector {

    String collectGuess(String input);

    String collectWordleResponse(String guess);
}
