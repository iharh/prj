package mdm.out;

public class MorphFS {
    private String lemma;
    private MorphFSAttr [] attrs;

    public String getLemma() {
        return lemma;
    }
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public MorphFSAttr [] getAttrs() {
        return attrs;
    }
    public void setAttrs(MorphFSAttr [] attrs) {
        this.attrs = attrs;
    }
};

