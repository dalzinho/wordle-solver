package uk.co.mrdaly.wordlehelper.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.co.mrdaly.wordlehelper.game.AnalysisGameFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class SelfPlayAnalysis {

    private final ExecutorService executorService = Executors.newFixedThreadPool(12);
    private final List<String> words;
    private final AtomicInteger completedThreadCount = new AtomicInteger();
    private final AnalysisGameFactory analysisGameFactory;

    public SelfPlayAnalysis(List<String> words, AnalysisGameFactory analysisGameFactory) {
        this.words = words;
        this.analysisGameFactory = analysisGameFactory;
    }


    public Map<String, Double> buildBestFirstGuessMap() {
        Map<String, List<Integer>> map = new HashMap<>();

        Deque<Future<Map<String, Integer>>> futures = new ArrayDeque<>();

        for (String word : words) {
            final SelfPlayCallable selfPlayCallable = new SelfPlayCallable(word, analysisGameFactory, words);
            final Future<Map<String, Integer>> submit = executorService.submit(selfPlayCallable);
            futures.add(submit);
        }

        while (!futures.isEmpty()) {
            Future<Map<String, Integer>> future = futures.pop();
            if (future.isDone()) {
                try {
                    final Map<String, Integer> stringIntegerMap = future.get();
                    log.info(stringIntegerMap.toString());
                    for (Map.Entry<String, Integer> entry : stringIntegerMap.entrySet()) {
                        map.putIfAbsent(entry.getKey(), new ArrayList<>());
                        map.get(entry.getKey()).add(entry.getValue());
                    }
                    int completedThreads = completedThreadCount.incrementAndGet();
                    if (completedThreads == words.size() || completedThreads % 100 == 0) {
                        log.info("Entry " + completedThreadCount.incrementAndGet() + " of " + words.size() + " completed at  " + LocalDateTime.now());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("error processing one of the threads", e);
                }
            } else {
                futures.add(future);
            }
        }



        Map<String, Double> result = new HashMap<>();

        map.keySet().stream()
                .forEach(key -> {
                    double average = map.get(key).stream().mapToInt(Integer::intValue).average().getAsDouble();
                    result.put(key, average);
                });
        return result;
    }
}
