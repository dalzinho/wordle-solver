package uk.co.mrdaly.wordlehelper.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SysInputCollector implements InputCollector {
    
    private final Scanner scanner;
    private final Output sysOutput;

    public SysInputCollector(Scanner scanner, Output sysOutput) {
        this.scanner = scanner;
        this.sysOutput = sysOutput;
    }

    @Override
    public String collectGuess() {
        String guess;

        while (true) {
            sysOutput.send("Enter your guess");
            guess = scanner.nextLine();
            if (guess.matches("[0-9]+") || guess.matches("[a-z]{5}")) {
                return guess;
            } else if (guess.matches("[byg]{5}")) {
                sysOutput.send("Looks like you entered the wordle response instead of a guess.");
            } else {
                sysOutput.send("input should be a five-letter word.");
            }
        }
    }
    

    @Override
    public String collectWordleResponse(String guess) {
        sysOutput.send("enter response: g for green, y for yellow, b for black");
        String respnse;
        
        while (true) {
            respnse = scanner.nextLine();
            if (!respnse.matches("[byg]{5}")) {
                sysOutput.send("the response is five-letter string that can only contain, g, y or b");
            } else {
                return respnse;
            }
        }
    }
}
