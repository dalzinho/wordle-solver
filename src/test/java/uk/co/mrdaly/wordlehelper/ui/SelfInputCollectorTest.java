package uk.co.mrdaly.wordlehelper.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelfInputCollectorTest {

    private SelfInputCollector selfInputCollector;

    @Before
    public void setup() {
        selfInputCollector = new SelfInputCollector("hello");
    }

    @Test
    public void correctGuessAllGreen() {
        final String actual = selfInputCollector.collectWordleResponse("hello");
        assertEquals("ggggg", actual);
    }

    @Test
    public void allWrongGuessAllBlack() {
        final String actual = selfInputCollector.collectWordleResponse("xxxxx");
        assertEquals("bbbbb", actual);
    }

    @Test
    public void reverseWordSomeGreenSomeYellow() {
        final String actual = selfInputCollector.collectWordleResponse("olleh");
        assertEquals("yygyy", actual);
    }

    @Test
    public void mixBlackYellowGreen() {
        final String actual = selfInputCollector.collectWordleResponse("holex");
        assertEquals("gygyb", actual);
    }



}