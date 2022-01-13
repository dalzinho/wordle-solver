package uk.co.mrdaly.wordlehelper.ui;

import org.springframework.stereotype.Component;

@Component
public class SysOutput implements Output{

    @Override
    public void send(String s) {
        System.out.println(s);
    }

}
