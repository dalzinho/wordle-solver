package uk.co.mrdaly.wordlehelper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class WordConfig {

    private List<String> words;

    @PostConstruct
    public void init() {
        try (InputStream inputStream = new ClassPathResource("wordle-dictionary.txt").getInputStream();
             BufferedReader r = new BufferedReader(new InputStreamReader(inputStream))) {
            words = r.lines()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("error loading wordList", e);
        }
    }

    @Bean
    public List<String> words() {
        return new ArrayList<>(words);
    }
}
