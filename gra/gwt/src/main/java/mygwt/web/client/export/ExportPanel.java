package mygwt.web.client.report;

import com.clarabridge.common.client.context.ApplicationContext;
//import com.clarabridge.common.client.widget.dialog.MessageDialog;

import com.clarabridge.foundation.client.csrf.CsrfFormPanel;
//import com.clarabridge.foundation.client.widget.dialog.SessionExpiredDialog;
import com.clarabridge.foundation.shared.model.StringUtilHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.Map;

public class ExportPanel extends CsrfFormPanel {
    //-------- Sentiments export --------
    public static final String SENT_EXPORT_PROJECTID = "projectId";
    public static final String SENT_EXPORT_RULES = "exportRules";
    public static final String SENT_EXPORT_WORDS = "exportWords";
    public static final String SENT_EXPORT_NAME = "exportName";
    public static final String SENT_EXPORT_DESCR = "exportDescription";
    //public static final String REPORTEXPORT_HIDDEN_EXPORT_FIELDS = "hiddenExportFields";

    // --------------- REPORT EXPORT CONSTANTS --------------------------------- //
    public static final String REPORTEXPORT_PROJECTID = "projectId";


    private final VerticalPanel fieldsPanel = new VerticalPanel();
    private final Map<String, Hidden> fields = new HashMap<String, Hidden>(); 
	
    public ExportPanel(String actionUrl) {
        Hidden projectId = new Hidden(REPORTEXPORT_PROJECTID, ApplicationContext.get().getProjectId() + "");
        fields.put(REPORTEXPORT_PROJECTID, projectId);
        fieldsPanel.add(projectId);

        //Hidden modelId = new Hidden(REPORTEXPORT_MODELID, ApplicationContext.get().getModelIdForNode() + "");
        //fieldsPanel.add(modelId);
		
    	this.add(fieldsPanel);
    	this.setAction(GWT.getHostPageBaseURL() + actionUrl);
    	this.setMethod(FormPanel.METHOD_POST);
    	
        this.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                String error = event.getResults();
                if (!(StringUtilHelper.isNullOrEmpty(error)) && !(error.startsWith("<pre"))) {
                    // TODO: impl later !!!
                    //if (error.contains("j_spring_security_check")) { // session timeout marker
                    //    SessionExpiredDialog.showDialog();
                    //} else {
                    //    MessageDialog.showError(error);
                    //}
                }
            }
        });
    }

    /**
     * Updates or creates new Hidden input for the field.
     * @param <T> type of value
     * @param name name of the field
     * @param value
     */
    public <T> void setField(String name, T value) {
        String sValue = value == null ? null : String.valueOf(value);
        Hidden field = fields.get(name);
        if (field == null) {
            field = new Hidden(name, sValue);
            fieldsPanel.add(field);
            fields.put(name, field);
        } else {
            field.setValue(sValue); 
        }
    }
	
    public <T>void addFields(Map<String, T> pFields) {
        if (pFields != null) {
            for (String key : pFields.keySet()) {
                T value = pFields.get(key);
                setField(key, value);
            }
        }
    }
}
