package mdm.out;

public class Word {
    private int id;
    private String word;
    private Analysis analysis;
    private int wordStartPos;
//    private int wordLength;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public Analysis getAnalysis() {
        return analysis;
    }
    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public int getWordStartPos() {
        return wordStartPos;
    }
    public void setWordStartPos(int wordStartPos) {
        this.wordStartPos = wordStartPos;
    }

    public int getWordLength() {
        return word == null ? 0 : word.length();
    }
};
