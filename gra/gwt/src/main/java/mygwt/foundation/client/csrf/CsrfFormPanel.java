package mygwt.foundation.client.csrf;

import mygwt.foundation.shared.model.StringUtilHelper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * CSRF protection enabled form. Should replace HTTP GET requests where possible. 
 */
public class CsrfFormPanel extends FormPanel {
	
    public enum Target { BLANK("_blank"), SELF("_self"), PARENT("_parent"), TOP("_top");
        private String value;
        
        private Target(String value) {
            this.value = value;
        }
        
        public String toString() {
            return value;    
        }
    };

    public CsrfFormPanel() { 
        super();
    }
    
    public CsrfFormPanel(Target target) {
        super(target.toString());
    }
	
//	/**
//	 * Create hidden field with security token.
//	 */
//	private void init() {
//		
//		setOpenInNewWindow(false);
//	}
	
    @Override
    public void submit() {
        //Window.alert("Submit:" + getAction());
        setMethod(METHOD_POST);

        // Add page security token.
        Widget widget = getWidget();
        if (widget == null) {
            widget = new SimplePanel(); 
            add(widget);
        }
        
        String pageTokenName = null; // CsrfUtils.getTokenName();
        String pageTokenValue = null; // CsrfUtils.getTokenValue();
        if (!StringUtilHelper.isNullOrEmpty(pageTokenName) && !StringUtilHelper.isNullOrEmpty(pageTokenValue)) {
            // Add security token parameter as part of URL for multipart forms.
            if (FormPanel.ENCODING_MULTIPART.equals(getEncoding())) {
                String csrfParam = pageTokenName + '=' + pageTokenValue;
                if (!getAction().toLowerCase().endsWith(csrfParam.toLowerCase())) {
                        setAction(getAction() 
                            + (getAction().contains("?") ? "&" : "?") 
                            + csrfParam); 
                }
            // Common forms.
            } else if (widget instanceof HasWidgets) { 
                //Window.alert("Token:" + pageTokenValue);
                Hidden hidden = new Hidden(pageTokenName, pageTokenValue);
                ((HasWidgets) widget).add(hidden);
            }
        }

        if (!isAttached()) {
            RootPanel.get().add(this); // Needed for FF and IE. Chrome and Safari are ok either.    
        }
        super.submit();
    }

    /**
     * Reloads current page using HTTP POST request.
     * Use this instead of Window.Location.reload().
     */
    public static void reload() {
        FormPanel form = new CsrfFormPanel(Target.SELF);
        form.setAction(Window.Location.getHref());
        form.submit();
    }
}
