package uk.co.mrdaly.wordlehelper.analysis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import uk.co.mrdaly.wordlehelper.analysis.frequency.FrequencyBasedWordListAnalyzer;
import uk.co.mrdaly.wordlehelper.game.AnalysisGameFactory;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;
import uk.co.mrdaly.wordlehelper.ui.NullOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class SelfPlayCallableTest {

    private List<String> allWords;
    private AnalysisGameFactory analysisGameFactory;

    public SelfPlayCallableTest() {
        try (InputStream inputStream = new ClassPathResource("all-wordle.txt").getInputStream();
             BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
             InputStream inputStream1 = new ClassPathResource("wordle-dictionary.txt").getInputStream();
             BufferedReader r2 = new BufferedReader(new InputStreamReader(inputStream1));
        ) {
            allWords = r.lines()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("error loading wordList", e);
        }

        WordFrequencyScorer wordFrequencyScorer = new WordFrequencyScorer();
        WordListAnalyzer wordListAnalyzer = new FrequencyBasedWordListAnalyzer(wordFrequencyScorer);
        analysisGameFactory = new AnalysisGameFactory(wordListAnalyzer, new NullOutput());
    }

    @Test
    public void setup() {
        SelfPlayCallable selfPlayCallable = new SelfPlayCallable("nymph", analysisGameFactory, allWords);
        final List<String> call = selfPlayCallable.call().entrySet().stream().sorted(Map.Entry.comparingByValue()).map(entry -> entry.getKey() + " -> " + entry.getValue()).collect(Collectors.toList());
        System.out.println(call);
    }


}