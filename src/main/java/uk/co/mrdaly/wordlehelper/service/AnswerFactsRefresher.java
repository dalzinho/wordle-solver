package uk.co.mrdaly.wordlehelper.service;

import lombok.extern.slf4j.Slf4j;
import uk.co.mrdaly.wordlehelper.guess.*;

import java.util.*;

@Slf4j
public class AnswerFactsRefresher {

    public AnswerFacts getMatches(AnswerFacts answerFacts, String guess, String response) {
        Map<String, Integer> letterAppearance = new HashMap<>();

        for (int i = 0; i < response.length(); i++) {
            char responseElement = response.charAt(i);
            String guessElement = guess.substring(i, i + 1);

            answerFacts.updatePositionFacts(i, guessElement, responseElement);
            if (responseElement != 'b') {
                letterAppearance.merge(guessElement, 1, Integer::sum);
            }
        }

        answerFacts.setLetterAppearance(letterAppearance);
        return answerFacts;
    }


}
