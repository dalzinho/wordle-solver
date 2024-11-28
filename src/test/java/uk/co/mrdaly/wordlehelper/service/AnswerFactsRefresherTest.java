package uk.co.mrdaly.wordlehelper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.mrdaly.wordlehelper.guess.AnswerFacts;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnswerFactsRefresherTest {

    private AnswerFactsRefresher wordMatcher;

    @BeforeEach
    void setup() {
        wordMatcher = new AnswerFactsRefresher();
    }
    @Test
    void twoWordsFiltersOne_responseSizeIsOne() {
        List<String> words = List.of("abcde","fghij");
        List<String> matches = wordMatcher.getMatches(words, new AnswerFacts(), "aqqqq", "gbbbb");
        assertEquals(1, matches.size());

    }
}