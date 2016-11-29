package mygwt.web.client.sentiments.rse;

import java.io.Serializable;

public class RecentSentimentExportsInfo implements Serializable {
    private String id;
    private String description;
    private String timestamp;
    private String parameters;
    private String fileName;

    // need for Serializable
    public RecentSentimentExportsInfo() {
    }

    public RecentSentimentExportsInfo(String id, String description, String timestamp, String parameters, String fileName) {
        this.id = id;
        this.description = description;
        this.timestamp = timestamp;
        this.parameters = parameters;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getParameters() {
        return parameters;
    }
    public String getFileName() {
        return fileName;
    }
}
