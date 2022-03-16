package uk.co.mrdaly.wordlehelper.ui;

import org.springframework.stereotype.Component;

@Component
public class NullOutput implements Output {
    @Override
    public void send(String s) {

    }
}
