package mygwt.portal.dto;

import java.io.Serializable;

import java.util.LinkedList;
import java.util.List;

public class SentimentUploadValidationResult implements Serializable {
    private static final long serialVersionUID = -572229596589406489L;

    private long    projectId;
    private String  sessionId;
    private int     wordsImported;
    private int     rulesImported;
    private List<SkippedInfo> skippedWordRows = new LinkedList<SkippedInfo>();
    private List<SkippedInfo> skippedRulesRows = new LinkedList<SkippedInfo>();
    private boolean isNegatorTuned = false;

    public SentimentUploadValidationResult(long projectId, String sessionId) {
        this.projectId = projectId;
        this.sessionId = sessionId;
    }

    public SentimentUploadValidationResult() {
        this.projectId = -1;
        this.sessionId = null;
    }

    public void addSkippedWord(SkippedInfo info) {
        skippedWordRows.add(info);
    }

    public void addSkippedRule(SkippedInfo info) {
        skippedRulesRows.add(info);
    }

    public int getWordsImported() {
        return wordsImported;
    }

    public void setWordsImported(int wordsImported) {
        this.wordsImported = wordsImported;
    }

    public int getRulesImported() {
        return rulesImported;
    }

    public void setRulesImported(int rulesImported) {
        this.rulesImported = rulesImported;
    }

    public List<SkippedInfo> getSkippedWordRows() {
        return skippedWordRows;
    }

    public void setSkippedWordRows(List<SkippedInfo> skippedWordRows) {
        this.skippedWordRows = skippedWordRows;
    }

    public List<SkippedInfo> getSkippedRulesRows() {
        return skippedRulesRows;
    }

    public void setSkippedRulesRows(List<SkippedInfo> skippedRulesRows) {
        this.skippedRulesRows = skippedRulesRows;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isNegatorTuned() {
        return isNegatorTuned;
    }

    public void setNegatorTuned(boolean isNegatorTuned) {
        this.isNegatorTuned = isNegatorTuned;
    }
}
