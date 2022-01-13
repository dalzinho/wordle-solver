package uk.co.mrdaly.wordlehelper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.mrdaly.wordlehelper.game.Game;
import uk.co.mrdaly.wordlehelper.service.WordFrequencyScorer;
import uk.co.mrdaly.wordlehelper.service.Wordlist;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class WordlehelperApplication implements CommandLineRunner {

    private Game game;

    public WordlehelperApplication(Game game) {
        this.game = game;
    }

    public static void main(String[] args) {
        SpringApplication.run(WordlehelperApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        game.run();
    }
}
