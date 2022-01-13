package uk.co.mrdaly.wordlehelper.game;

import org.springframework.stereotype.Service;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;
import uk.co.mrdaly.wordlehelper.service.Wordlist;
import uk.co.mrdaly.wordlehelper.ui.InputCollector;
import uk.co.mrdaly.wordlehelper.ui.Output;

import java.util.List;

@Service
public class Game {

    private final Wordlist wordlist;
    private final WordFrequencyScorer wordFrequencyScorer;
    private final WordListAnalyzer wordListAnalyzer;
    private final InputCollector sysInputCollector;
    private final Output sysOutput;

    public Game(Wordlist wordlist, WordFrequencyScorer wordFrequencyScorer, WordListAnalyzer wordListAnalyzer, InputCollector sysInputCollector, Output sysOutput) {
        this.wordlist = wordlist;
        this.wordFrequencyScorer = wordFrequencyScorer;
        this.wordListAnalyzer = wordListAnalyzer;
        this.sysInputCollector = sysInputCollector;
        this.sysOutput = sysOutput;
    }

    public void run() {
        boolean run = true;

        String guess = null;

        while (run) {
            if (guess == null) {
                guess = sysInputCollector.collectGuess();
            }

            if (guess.matches("[0-9]+")) {
                int i = Integer.parseInt(guess);
                sysOutput.send(wordlist.getByIndex(i));
                break;
            }

            final String response = sysInputCollector.collectWordleResponse();

            List<String> answers = wordlist.getMatches(guess, response);
            guess = wordListAnalyzer.getNextBestGuess(answers, ".....");
            System.out.println(guess);
            run = (answers.size() > 1);
        }
    }
}
