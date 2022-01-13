package uk.co.mrdaly.wordlehelper.service;

public class WordWithFrequency {

    private final String word;
    private final double frequency;

    public WordWithFrequency(String word, String frequency) {
        this.word = word;
        this.frequency = Double.parseDouble(frequency);
    }

    public WordWithFrequency(String word, double frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public double getFrequency() {
        return frequency;
    }
}
