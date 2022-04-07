package uk.co.mrdaly.wordlehelper.analysis.frequency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScrabbleValueBasedTolerance {

    private final Map<String, Integer> scrabbleTileScores;

    public ScrabbleValueBasedTolerance(Map<String, Integer> scrabbleTileScores) {
        this.scrabbleTileScores = scrabbleTileScores;
    }

    public String getRegexForTolerance(int maxScore) {
        final String includedLetters = scrabbleTileScores.entrySet().stream()
                .filter(entry -> entry.getValue() <= maxScore)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining());
        return "[" + includedLetters.toLowerCase(Locale.ROOT) + "]+";
    }
}
