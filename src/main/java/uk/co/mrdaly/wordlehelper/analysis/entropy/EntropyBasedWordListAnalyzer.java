package uk.co.mrdaly.wordlehelper.analysis.entropy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.ui.SelfInputCollector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Component
@Slf4j
public class EntropyBasedWordListAnalyzer implements WordListAnalyzer {

    private final List<String> words;
    private final List<String> alreadyGuessed;

    public EntropyBasedWordListAnalyzer(List<String> words) {
        this.words = words;
        alreadyGuessed = new ArrayList<>();
    }



    @Override
    public String getNextBestGuess(List<String> possibilities, String wordRegex) {
        LocalDateTime start = LocalDateTime.now();
        String guess = words
                .parallelStream()
                .filter(word -> !alreadyGuessed.contains(word))
                .map(word -> calculateEntropyForGuess(word, possibilities))
                .max(Comparator.comparing(WordEntropy::entropy))
                .orElse(new WordEntropy("", 0.0))
                .word;

        LocalDateTime end = LocalDateTime.now();
        log.info("calculated next guess in {}", Duration.between(start, end));
        alreadyGuessed.add(guess);
        return guess;

    }

    private WordEntropy calculateEntropyForGuess(String guess, List<String> possiblities) {
        Map<Integer, Integer> buckets = new HashMap<>();
        for (String possibility : possiblities) {
            int entropyScore = scoreWord(possibility, guess);
            buckets.merge(entropyScore, 1, Integer::sum);
        }

        double entropy = buckets.values().stream()
                .map(i -> (double) i / words.size())
                .mapToDouble(Math::log)
                .sum() * -1;
        return new WordEntropy(guess, entropy);
    }


    private record WordEntropy(String word, double entropy) {
    }

    private int scoreWord(String possibility, String guess) {
        final String wordleResponse = new SelfInputCollector(possibility).collectWordleResponse(guess);
        return Stream.of(wordleResponse.split(""))
                .mapToInt(this::score)
                .sum();
    }

    private int score(String s) {
        int i = 0;
        if (s.equals("y")) {
            i = 5;
        } else if (s.equals("g")) {
            i = 13;
        }
        return i;
    }
}
