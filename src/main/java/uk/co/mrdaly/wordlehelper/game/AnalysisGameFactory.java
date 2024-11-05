package uk.co.mrdaly.wordlehelper.game;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.WordMatcher;
import uk.co.mrdaly.wordlehelper.ui.NullOutput;
import uk.co.mrdaly.wordlehelper.ui.SelfInputCollector;

import java.util.ArrayList;

@Component
public class AnalysisGameFactory {

    private final WordListAnalyzer wordListAnalyzer;
    private final NullOutput nullOutput;

    public AnalysisGameFactory(WordListAnalyzer entropyBasedWordListAnalyzer, NullOutput nullOutput) {
        this.wordListAnalyzer = entropyBasedWordListAnalyzer;
        this.nullOutput = nullOutput;
    }

    public Game get(String word) {
        return new Game(new WordMatcher(), wordListAnalyzer, new ArrayList<>(), new SelfInputCollector(word), nullOutput);
    }
}
