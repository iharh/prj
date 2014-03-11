package mdm.out;

public class OutSeg {
    private String id;
    private SegInfo segInfo; // we don't need this, keep for XStream only
    private Word [] words;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    //public SegInfo getSegInfo() {
    //    return segInfo;
    //}
    //public void setSegInfo(SegInfo segInfo) {
    //    this.segInfo = segInfo;
    //}

    public Word [] getWords() {
        return words;
    }
    public void setWords(Word [] words) {
        this.words = words;
    }
};

