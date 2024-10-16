package uk.co.mrdaly.wordlehelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.game.Game;
import uk.co.mrdaly.wordlehelper.service.WordMatcher;
import uk.co.mrdaly.wordlehelper.ui.InputCollector;
import uk.co.mrdaly.wordlehelper.ui.Output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Configuration
public class BeanConfiguration {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public Game guessingGame(WordMatcher wordMatcher,
                             WordListAnalyzer entropyBasedWordListAnalyzer,
                             InputCollector sysInputCollector,
                             Output sysOutput) {
        return new Game(wordMatcher, entropyBasedWordListAnalyzer, sysInputCollector, sysOutput);
    }

    @Bean
    public WordMatcher wordMatcher() {
        return new WordMatcher();
    }

    @Bean
    public Map<String, Integer> scrabbleTileScores() {
        Map<String, Integer> tileValues = new HashMap<>();
        // scrabble tile value / scrabble tile frequency
        tileValues.put("A", 1);
        tileValues.put("B", 3);
        tileValues.put("C", 3);
        tileValues.put("D", 3);
        tileValues.put("E", 1);
        tileValues.put("F", 4);
        tileValues.put("G", 2);
        tileValues.put("H", 4);
        tileValues.put("I", 1);
        tileValues.put("J", 8);
        tileValues.put("K", 5);
        tileValues.put("L", 1);
        tileValues.put("M", 3);
        tileValues.put("N", 1);
        tileValues.put("O", 1);
        tileValues.put("P", 3);
        tileValues.put("Q", 10);
        tileValues.put("R", 1);
        tileValues.put("S", 1);
        tileValues.put("T", 1);
        tileValues.put("U", 1);
        tileValues.put("V", 4);
        tileValues.put("W", 4);
        tileValues.put("X", 8);
        tileValues.put("Y", 4);
        tileValues.put("Z", 10);
        return tileValues;
    
}
}
