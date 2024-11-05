package uk.co.mrdaly.wordlehelper.guess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerFactsTest {

    private AnswerFacts answerFacts;

    @BeforeEach
    void setup() {
        answerFacts = new AnswerFacts();
    }

    @Test
    void guessIsShell_givesBBBBG_correctGuessIsL_wrongGuessesAreSHE() {
        char[] response = {'b','b','b','b','g'};
        for (int i = 0; i < "shell".length(); i++) {
            answerFacts.updatePositionFacts(i, "shell".substring(i, i+1), response[i] );
        }
        assertTrue(answerFacts.wordMatchesRegex("vinyl"));
        assertTrue(answerFacts.wordDoesNotContainAWrongGuess("vinyl"));
        assertFalse(answerFacts.wordDoesNotContainAWrongGuess("shine"));
    }
}