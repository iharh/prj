package mdm.in;

public class InDoc {
    private String id;
    private InSeg [] segs;

    public InDoc(String id, InSeg[] segs) {
        this.id = id;
        this.segs = segs;
    }

    public String getId() {
        return id;
    }

    public InSeg [] getSegs() {
        return segs;
    }
};

