package uk.co.mrdaly.wordlehelper.analysis;

import uk.co.mrdaly.wordlehelper.service.Wordlist;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordListAnalyzer {

    private final Wordlist wordlist;

    public WordListAnalyzer(Wordlist wordlist) {
        this.wordlist = wordlist;
    }

    public List<String> sort(List<String> possibilities) {
        final Map<String, Integer> letterFrequencies = getLetterFrequencies(possibilities);

        return possibilities.stream()
                .map(word -> scoreWord(word, letterFrequencies))
                .sorted(Comparator.comparing(WordWithFrequency::getScore, Comparator.reverseOrder()))
                .map(WordWithFrequency::getWord)
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

    private WordWithFrequency scoreWord(String word, Map<String, Integer> frequencyMap) {
        int i = 0;

        for (String letter : word.split("")) {
            i += frequencyMap.get(letter);
        }

        return new WordWithFrequency(word, i);
    }

    public static class WordWithFrequency {
        private String word;
        private int score;

        public WordWithFrequency(String word, int score) {
            this.word = word;
            this.score = score;
        }

        public String getWord() {
            return word;
        }

        public int getScore() {
            return score;
        }
    }

}
