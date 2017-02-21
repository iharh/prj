package mygwt.portal.dto;

import java.io.Serializable;

public class SkippedInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // Number of row in Excel file
    private int rowNum;

    // Word or cb_id to search for.
    private String hint;

    private String errMessage;

    public SkippedInfo() {
    }

    public SkippedInfo(int rowNum, String hint, String errMessage) {
        super();
        this.rowNum = rowNum;
        this.hint = hint;
        this.errMessage = errMessage;
    }

    public int getRowNum() {
        return rowNum;
    }

    public String getHint() {
        return hint;
    }
    public String getErrMessage() {
        return errMessage;
    }
}
