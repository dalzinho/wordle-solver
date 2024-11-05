package uk.co.mrdaly.wordlehelper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "wordlist")
@Slf4j
public class WordConfig {
    private List<String> filenames = new ArrayList<>();

    public List<String> getFilenames() {
        return this.filenames;
    }
    @Bean
    @Primary
    public List<String> wordlists() {
        return filenames.stream()
                .map(this::loadWordlist)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    private Optional<List<String>> loadWordlist(String filename) {
        try (InputStream inputStream = new ClassPathResource(filename).getInputStream();
             BufferedReader r = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> result = r.lines()
                    .map(String::trim)
                    .collect(Collectors.toList());
            return Optional.of(result);
        } catch (IOException e) {
            log.error("error loading wordList", e);
        }
        return Optional.empty();
    }

    @Bean
    public List<String> commonAnswers() {
        return loadWordlist("wordle-dictionary.txt").get();
    }
}
