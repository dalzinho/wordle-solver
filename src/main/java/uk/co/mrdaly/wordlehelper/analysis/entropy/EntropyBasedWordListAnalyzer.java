package uk.co.mrdaly.wordlehelper.analysis.entropy;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.ui.SelfInputCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntropyBasedWordListAnalyzer implements WordListAnalyzer {

    private List<String> words;

    public EntropyBasedWordListAnalyzer(List<String> words) {
        this.words = words;
    }


    @Override
    public String getNextBestGuess(List<String> possiblities, String wordRegex) {
        double maxEntropy = -1;
        String maxEntropyWord = "";

        for (String guess : words) {
            Map<Integer, Integer> buckets = new HashMap<>();
            for (String possibility : possiblities) {
                int entropyScore = scoreWord(possibility, guess);
                buckets.merge(entropyScore, 1, Integer::sum);
            }

            double entropy = buckets.values().stream()
                    .map(i -> (double) i / words.size())
                    .mapToDouble(Math::log)
                    .sum() * -1;

            if (entropy > maxEntropy) {
                maxEntropy = entropy;
                maxEntropyWord = guess;
            }
        }

        words.remove(maxEntropyWord);
        return maxEntropyWord;
    }

    private int scoreWord(String possibility, String guess) {
        final String wordleResponse = new SelfInputCollector(possibility).collectWordleResponse(guess);
        int i = 0;

        for (String s : wordleResponse.split("")) {
            if (s.equals("y")) {
                i += 2;
            } else if (s.equals("g")) {
                i += 5;
            }
        }

        return i;
    }
}
