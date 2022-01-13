package uk.co.mrdaly.wordlehelper.game;

import org.springframework.stereotype.Service;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;
import uk.co.mrdaly.wordlehelper.service.Wordlist;
import uk.co.mrdaly.wordlehelper.ui.InputCollector;
import uk.co.mrdaly.wordlehelper.ui.Output;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Game {

    private final Wordlist wordlist;
    private final WordFrequencyScorer wordFrequencyScorer;
    private final InputCollector sysInputCollector;
    private final Output sysOutput;

    public Game(Wordlist wordlist, WordFrequencyScorer wordFrequencyScorer, InputCollector sysInputCollector, Output sysOutput) {
        this.wordlist = wordlist;
        this.wordFrequencyScorer = wordFrequencyScorer;
        this.sysInputCollector = sysInputCollector;
        this.sysOutput = sysOutput;
    }

    public void run() {
        boolean run = true;

        while (run) {

            final String guess = sysInputCollector.collectGuess();

            if (guess.matches("[0-9]+")) {
                int i = Integer.parseInt(guess);
                sysOutput.send(wordlist.getByIndex(i));
                break;
            }

            final String response = sysInputCollector.collectWordleResponse();

            List<Map.Entry<String, Double>> lastAnswers = wordlist.getMatches(guess, response)
                    .stream()
                    .collect(Collectors.toMap(s -> s, wordFrequencyScorer::calculateFrequencyScore))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                    .collect(Collectors.toList());


            NumberFormat formatter = new DecimalFormat("#0.0000");
            lastAnswers.subList(0, Math.min(lastAnswers.size(), 5))
                    .stream()
                    .map(entry -> entry.getKey() + " -> " + formatter.format(entry.getValue() * 100))
                    .forEach(sysOutput::send);

            run = (lastAnswers.size() > 1);
        }
    }
}
