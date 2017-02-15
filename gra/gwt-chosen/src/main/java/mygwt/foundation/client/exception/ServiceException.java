package mygwt.foundation.client.exception;

import java.io.Serializable;
import java.util.Date;

public class ServiceException extends Exception implements Serializable {
    private static final long serialVersionUID = -210839837255594840L;
    
    private Date runDate;
    private int resultSource;
    
    private String message;
    
    public ServiceException() {
        this.runDate = new Date();
        this.resultSource = 0;
    }
    
    public ServiceException(String message) {
        super(message);
        this.message = message;
        this.runDate = new Date();
        this.resultSource = 0;
    }
    
    public ServiceException(String message, Date runDate, int resultSource) {
        super(message);
        this.message = message;
        this.runDate = runDate;
        this.resultSource = resultSource;
    }
    
    public ServiceException(Date runDate, int resultSource) {
        super();
        this.runDate = runDate;
        this.resultSource = resultSource;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public int getResultSource() {
        return resultSource;
    }

    public void setResultSource(int resultSource) {
        this.resultSource = resultSource;
    }

    public String getMessage() {
        return message;
    }
}
