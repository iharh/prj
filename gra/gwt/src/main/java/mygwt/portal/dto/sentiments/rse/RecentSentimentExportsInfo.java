package mygwt.portal.dto.sentiments.rse;

import java.io.Serializable;

public class RecentSentimentExportsInfo implements Serializable {
    private String fileName;
    private String name;
    private String timestamp;
    private boolean words;
    private boolean rules;

    // need for Serializable
    public RecentSentimentExportsInfo() {
    }

    public RecentSentimentExportsInfo(String fileName, String name, String timestamp, boolean words, boolean rules) {
        this.fileName = fileName;
        this.name = name;
        this.timestamp = timestamp;
        this.words = words;
        this.rules = rules;
    }

    public String getFileName() {
        return fileName;
    }
    public String getName() {
        return name;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public boolean isWords() {
        return words;
    }
    public boolean isRules() {
        return rules;
    }
}
