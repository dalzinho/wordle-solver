package uk.co.mrdaly.wordlehelper.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Component
public class WordFrequencyScorer implements ToDoubleFunction<String> {

    private static Map<String, Double> frequencyScoreMap;

    static {
        frequencyScoreMap = new HashMap<>();
        // scrabble tile value / scrabble tile frequency
        frequencyScoreMap.put("A", 1d / 9);
        frequencyScoreMap.put("B", 3d / 2);
        frequencyScoreMap.put("C", 3d / 2);
        frequencyScoreMap.put("D", 3d / 2);
        frequencyScoreMap.put("E", 1d / 12);
        frequencyScoreMap.put("F", 4d / 2);
        frequencyScoreMap.put("G", 2d / 3);
        frequencyScoreMap.put("H", 4d / 2);
        frequencyScoreMap.put("I", 1d / 9);
        frequencyScoreMap.put("J", 8d / 1);
        frequencyScoreMap.put("K", 5d / 1);
        frequencyScoreMap.put("L", 1d / 4);
        frequencyScoreMap.put("M", 3d / 2);
        frequencyScoreMap.put("N", 1d / 6);
        frequencyScoreMap.put("O", 1d / 8);
        frequencyScoreMap.put("P", 3d / 2);
        frequencyScoreMap.put("Q", 10d / 1);
        frequencyScoreMap.put("R", 1d / 6);
        frequencyScoreMap.put("S", 1d / 4);
        frequencyScoreMap.put("T", 1d / 6);
        frequencyScoreMap.put("U", 1d / 4);
        frequencyScoreMap.put("V", 4d / 2);
        frequencyScoreMap.put("W", 4d / 2);
        frequencyScoreMap.put("X", 8d / 1);
        frequencyScoreMap.put("Y", 4d / 2);
        frequencyScoreMap.put("Z", 10d / 1);
    }

    public double getFrequencyScoreFor(String s) {
        return frequencyScoreMap.get(s.toUpperCase(Locale.ROOT));
    }

    public double getFrequency(Map.Entry<String, Integer> entry) {
        return frequencyScoreMap.get(entry.getKey());
    }

    @Override
    public double applyAsDouble(String value) {
        return getFrequencyScoreFor(value);
    }
}
