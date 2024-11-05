package uk.co.mrdaly.wordlehelper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.guess.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WordMatcher {

    public List<String> getMatches(List<String> words, AnswerFacts answerFacts, String guess, String response) {
        Map<String, Integer> letterAppearance = new HashMap<>();

        for (int i = 0; i < response.length(); i++) {
            char responseElement = response.charAt(i);
            String guessElement = guess.substring(i, i + 1);

            answerFacts.updatePositionFacts(i, guessElement, responseElement);
            if (responseElement != 'b') {
                letterAppearance.merge(guessElement, 1, Integer::sum);
            }
        }

        return words.stream()
                .filter(answerFacts::wordDoesNotContainAWrongGuess)
                .filter(answerFacts::wordMatchesRegex)
                .filter(word -> containsKnownInexactGuesses(word, letterAppearance))
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
}
