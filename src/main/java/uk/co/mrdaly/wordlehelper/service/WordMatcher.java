package uk.co.mrdaly.wordlehelper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.guess.CorrectGuess;
import uk.co.mrdaly.wordlehelper.guess.Guess;
import uk.co.mrdaly.wordlehelper.guess.InexactGuess;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WordMatcher {

    Set<String> wrongGuesses;
    Map<String, Integer> letterAppearance;
    Guess[] regexPattern = new Guess[]{null, null, null, null, null};

    public WordMatcher() {
        wrongGuesses = new HashSet<>();
    }

    public List<String> getMatches(List<String> words, String guess, String response) {
        letterAppearance = new HashMap<>();

        for (int i = 0; i < response.length(); i++) {
            char responseElement = response.charAt(i);
            String guessElement = guess.substring(i, i + 1);

            if (responseElement == 'g') {
                regexPattern[i] = new CorrectGuess(guessElement);
                letterAppearance.merge(guessElement, 1, Integer::sum);
            } else {
                Guess g = regexPattern[i];

                if (g == null) {
                    regexPattern[i] = new InexactGuess();
                }

                if (!(g instanceof CorrectGuess)) {
                    ((InexactGuess) regexPattern[i]).updateGuess(guessElement);
                }


                if (responseElement == 'y') {
                    letterAppearance.merge(guessElement, 1, Integer::sum);
                } else {
                    wrongGuesses.add(guessElement);
                }
            }
        }

        wrongGuesses.removeAll(letterAppearance.keySet());

        return getMatches(words);
    }



    private String getKnownRegex() {
        StringBuilder sb = new StringBuilder();

        for (Guess guess : regexPattern) {
            String next = null == guess ? "." : guess.getRegexElement();
            sb.append(next);
        }
        return sb.toString();
    }

    public List<String> getMatches(List<String> words) {
        return words.stream()
                .filter(this::doesNotContainAWrongGuess)
                .filter(this::matchesRegexPattern)
                .filter(this::containsKnownInexactGuesses)
                .collect(Collectors.toList());
    }

    private boolean doesNotContainAWrongGuess(String word) {
        return wrongGuesses.stream().noneMatch(word::contains);
    }

    private boolean matchesRegexPattern(String word) {
        return word.matches(getKnownRegex());
    }

    private boolean containsKnownInexactGuesses(String word) {
        Map<String, Integer> count = new HashMap<>();
        for (String s : word.split("")) {
            count.merge(s, 1, Integer::sum);
        }

        return letterAppearance.entrySet().stream()
                .allMatch(entry -> count.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }
}
