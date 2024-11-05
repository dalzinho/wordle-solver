package uk.co.mrdaly.wordlehelper.analysis;

import uk.co.mrdaly.wordlehelper.game.AnalysisGameFactory;
import uk.co.mrdaly.wordlehelper.guess.AnswerFacts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SelfPlayCallable implements Callable<Map<String, Integer>> {

    private final String word;
    private final AnalysisGameFactory analysisGameFactory;
    private final List<String> words;

    public SelfPlayCallable(String word, AnalysisGameFactory analysisGameFactory, List<String> words) {
        this.word = word;
        this.analysisGameFactory = analysisGameFactory;
        this.words = words;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> map = new HashMap<>();

        for (String firstGuess : words) {
            final int guessCount = analysisGameFactory.get(word).run(firstGuess, new AnswerFacts(), words, 1);
            map.put(firstGuess, guessCount);
        }

        return map;
    }
}
