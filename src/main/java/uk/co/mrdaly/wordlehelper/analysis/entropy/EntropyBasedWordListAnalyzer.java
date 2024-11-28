package uk.co.mrdaly.wordlehelper.analysis.entropy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final List<String> commonAnswers;

    public EntropyBasedWordListAnalyzer(@Qualifier("wordlists") List<String> words, @Qualifier("commonAnswers") List<String> commonAnswers) {
        this.words = words;
        this.commonAnswers = commonAnswers;
        alreadyGuessed = new ArrayList<>();
    }

    @Override
    public String getNextBestGuess(List<String> possibilities, String wordRegex) {
        if (possibilities.size() == 1) {
            return possibilities.get(0);
        }
        LocalDateTime start = LocalDateTime.now();
        List<WordEntropy> sortedEntropyList = words
                .parallelStream()
                .filter(word -> !alreadyGuessed.contains(word))
                .map(word -> new WordEntropy(word, 0d))
                .map(wordEntropy -> calculateEntropyForGuess(wordEntropy, possibilities))
                .map(wordEntropy -> calculateEntropyForGuess(wordEntropy, commonAnswers, 3))
                .sorted(Comparator.comparing(WordEntropy::entropy, Comparator.reverseOrder()))
                .toList();

        String guess = sortedEntropyList.get(0).word;
        LocalDateTime end = LocalDateTime.now();
        log.info("calculated next guess in {}", Duration.between(start, end));
        alreadyGuessed.add(guess);
        return guess;

    }

    private WordEntropy calculateEntropyForGuess(WordEntropy guess, List<String> possibilities) {
        return calculateEntropyForGuess(guess, possibilities, 1);
    }

    private WordEntropy calculateEntropyForGuess(WordEntropy guess, List<String> possiblities, int entropyWeight) {
        Map<Integer, Integer> buckets = new HashMap<>();
        for (String possibility : possiblities) {
            int entropyScore = scoreWord(possibility, guess.word);
            buckets.merge(entropyScore, 1, Integer::sum);
        }

        double entropy = buckets.values().stream()
                .map(i -> (double) i / words.size())
                .mapToDouble(Math::log)
                .sum() * -1;


        return new WordEntropy(guess.word, guess.entropy + (entropy * entropyWeight));
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
        return switch (s) {
            case "y" -> 5;
            case "g" -> 13;

            default -> 0;
        };
    }
}
