package uk.co.mrdaly.wordlehelper.ui;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelfInputCollector implements InputCollector {

    private final String word;

    public SelfInputCollector(String word) {
        this.word = word;
    }

    @Override
    public String collectGuess() {
        return null;
    }

    @Override
    public String collectWordleResponse(String input) {
        List<String> letters = Arrays.asList(input.split(""));
        String[] result = new String[5];

        List<String> remainingLetters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (word.substring(i, i + 1).equals(letters.get(i))) {
                result[i] = "g";
            } else {
                remainingLetters.add(word.substring(i, i + 1));
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == null) {
                String wordLetter = input.substring(i, i + 1);
                if (remainingLetters.contains(wordLetter)) {
                    result[i] = "y";
                    remainingLetters.remove(wordLetter);
                } else {
                    result[i] = "b";
                }
            }
        }

        return String.join("", result);
    }

    public String getWord() {
        return word;
    }
}
