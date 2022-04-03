package uk.co.mrdaly.wordlehelper.analysis.frequency;

public  class WordWithFrequency {
    private String word;
    private double score;

    public WordWithFrequency(String word, double score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "WordWithFrequency{" +
                "word='" + word + '\'' +
                ", score=" + score +
                '}';
    }
}