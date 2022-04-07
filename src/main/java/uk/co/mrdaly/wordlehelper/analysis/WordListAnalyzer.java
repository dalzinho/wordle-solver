package uk.co.mrdaly.wordlehelper.analysis;

import java.util.List;

public interface WordListAnalyzer {
    String getNextBestGuess(List<String> possiblities, String wordRegex);
}
