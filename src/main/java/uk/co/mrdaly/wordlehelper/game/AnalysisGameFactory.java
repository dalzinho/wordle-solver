package uk.co.mrdaly.wordlehelper.game;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.WordMatcher;
import uk.co.mrdaly.wordlehelper.ui.NullOutput;
import uk.co.mrdaly.wordlehelper.ui.SelfInputCollector;

import java.util.List;

@Component
public class AnalysisGameFactory {

    private final WordListAnalyzer wordListAnalyzer;
    private final NullOutput nullOutput;
    private final List<String> words;

    public AnalysisGameFactory(WordListAnalyzer entropyBasedWordListAnalyzer, NullOutput nullOutput, List<String> words) {
        this.wordListAnalyzer = entropyBasedWordListAnalyzer;
        this.nullOutput = nullOutput;
        this.words = words;
    }

    public Game get(String word) {
        return new Game(new WordMatcher(), wordListAnalyzer, new SelfInputCollector(word), nullOutput, words);
    }
}
