package uk.co.mrdaly.wordlehelper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.mrdaly.wordlehelper.game.Game;
import uk.co.mrdaly.wordlehelper.guess.AnswerFacts;

import java.util.List;

@SpringBootApplication
@Slf4j
public class WordlehelperApplication implements CommandLineRunner {

    private final Game game;
    private final List<String> words;

    public WordlehelperApplication(Game game, @Qualifier("wordlists") List<String> words) {
        this.game = game;
        this.words = words;
    }

    public static void main(String[] args) {
        SpringApplication.run(WordlehelperApplication.class, args);
    }


    @Override
    public void run(String... args) {
        game.run("crane", new AnswerFacts(), words, 1);
    }
}
