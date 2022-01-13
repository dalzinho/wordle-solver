package uk.co.mrdaly.wordlehelper.analysis;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;
import uk.co.mrdaly.wordlehelper.service.Wordlist;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WordListAnalyzer {

    private final WordFrequencyScorer wordFrequencyScorer;

    public WordListAnalyzer(Wordlist wordlist, WordFrequencyScorer wordFrequencyScorer) {
        this.wordFrequencyScorer = wordFrequencyScorer;
    }

    public List<WordWithFrequency> sort(List<String> possibilities) {
        final Map<String, Integer> letterFrequencies = getLetterFrequencies(possibilities);

        return possibilities.stream()
                .map(word -> scoreWord(word, letterFrequencies))
                .sorted(Comparator.comparing(WordWithFrequency::getScore))
//                .map(WordWithFrequency::getWord)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getLetterFrequencies(List<String> possibilities) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : possibilities) {
            for (String letter : word.split("")) {
                map.merge(letter, 1, Integer::sum);
            }
        }
        return map;
    }

    public String getNextBestGuess(List<String> possiblities, String wordRegex) {
        if (wordRegex.matches("[a-z]{5}")) {
            return wordRegex;
        }

        Map<Integer, WordWithFrequency> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            if (wordRegex.charAt(i) == '.') {
                map.put(i, getMostFrequentLetterInPosition(i, possiblities));
            }
        }

        final Map.Entry<Integer, WordWithFrequency> integerWordWithFrequencyEntry = map.entrySet().stream().max(Comparator.comparing(entry -> entry.getValue().getScore())).stream().findFirst().get();
        int position = integerWordWithFrequencyEntry.getKey();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (i == position) {
                sb.append(integerWordWithFrequencyEntry.getValue().getWord());
            } else {
                sb.append(wordRegex.charAt(i));
            }
        }

        String next = sb.toString();
        final List<String> nextPossibilitySet = possiblities.stream().filter(word -> word.matches(next)).collect(Collectors.toList());
        return getNextBestGuess(nextPossibilitySet, next);

    }

    private WordWithFrequency getMostFrequentLetterInPosition(int i, List<String> possibilities) {

        List<String> lettersAtI = possibilities.stream()
                .map(word -> word.substring(i, i + 1))
                .collect(Collectors.toList());
        final Map<String, Integer> letterFrequencies = getLetterFrequencies(lettersAtI);

        return letterFrequencies.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(entry -> new WordWithFrequency(entry.getKey(), entry.getValue()))
                .orElse(null);
    }



    private WordWithFrequency scoreWord(String word, Map<String, Integer> frequencyMap) {
        double d = wordFrequencyScorer.calculateFrequencyScore(word);

        return new WordWithFrequency(word, d);
    }

    public static class WordWithFrequency {
        private String word;
        private double score;

        public WordWithFrequency(String word, double score) {
            this.word = word;
            this.score = score;
        }

        public String getWord() {
            return word;
        }

        public double getScore() {
            return score;
        }

        @Override
        public String toString() {
            return "WordWithFrequency{" +
                    "word='" + word + '\'' +
                    ", score=" + score +
                    '}';
        }
    }

}
