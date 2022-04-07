package uk.co.mrdaly.wordlehelper.analysis.frequency;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.NoWordFoundException;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FrequencyBasedWordListAnalyzer implements WordListAnalyzer {

    private WordFrequencyScorer wordFrequencyScorer;

    public FrequencyBasedWordListAnalyzer(WordFrequencyScorer wordFrequencyScorer) {
        this.wordFrequencyScorer = wordFrequencyScorer;
    }

    @Override
    public String getNextBestGuess(List<String> possiblities, String wordRegex) {
        if (wordRegex.matches("[a-z]{5}")) {
            return wordRegex;
        }

        Map<Integer, WordWithFrequency> map = new HashMap<>();
        boolean isLastLetter = checkForLastLetter(wordRegex);
        for (int i = 0; i < 5; i++) {
            if (wordRegex.charAt(i) == '.') {
                map.put(i, getMostFrequentLetterInPosition(i, possiblities, isLastLetter));
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

    private boolean checkForLastLetter(String wordRegex) {
        return Arrays.stream(wordRegex.split(""))
                .filter(letter -> letter.matches("[a-z]"))
                .count() == 4;
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


    private WordWithFrequency getMostFrequentLetterInPosition(int i, List<String> possibilities, boolean isLastLetter) {

        List<String> lettersAtI = possibilities.stream()
                .map(word -> word.substring(i, i + 1))
                .collect(Collectors.toList());
        final Map<String, Integer> letterFrequencies = getLetterFrequencies(lettersAtI);

        return isLastLetter ? getMostFrequentIfLast(letterFrequencies) : getMostFrequent(letterFrequencies);

    }

    private WordWithFrequency getMostFrequent(Map<String, Integer> letterFrequencies) {
        return letterFrequencies.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new WordWithFrequency(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    private WordWithFrequency getMostFrequentIfLast(Map<String, Integer> letterFrequencies) {
        int frequencyOfMostFrequent = letterFrequencies.values().stream().mapToInt(Integer::intValue).max().orElseThrow(NoWordFoundException::new);

        return letterFrequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == frequencyOfMostFrequent)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingDouble(wordFrequencyScorer))
                .map(s -> new WordWithFrequency(s, frequencyOfMostFrequent))
                .findFirst().orElse(null);
    }



}
