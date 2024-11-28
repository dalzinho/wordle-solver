package uk.co.mrdaly.wordlehelper.game;

import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.analysis.WordListAnalyzer;
import uk.co.mrdaly.wordlehelper.service.AnswerFactsRefresher;
import uk.co.mrdaly.wordlehelper.ui.NullOutput;
import uk.co.mrdaly.wordlehelper.ui.SelfInputCollector;

@Component
public class AnalysisGameFactory {

    private final WordListAnalyzer wordListAnalyzer;
    private final NullOutput nullOutput;

    public AnalysisGameFactory(WordListAnalyzer entropyBasedWordListAnalyzer, NullOutput nullOutput) {
        this.wordListAnalyzer = entropyBasedWordListAnalyzer;
        this.nullOutput = nullOutput;
    }

    public Game get(String word) {
        return new Game(new AnswerFactsRefresher(), wordListAnalyzer, new SelfInputCollector(word), nullOutput);
    }
}
