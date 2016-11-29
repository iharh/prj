package mygwt.foundation.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface CommonClientMessages extends Messages {
    CommonClientMessages INSTANCE = GWT.create(CommonClientMessages.class);
  
    /**
    * Translated "<b>Session expired.</b><br/> User is not authenticated or session timeout expired. Press OK to redirect to the Login Page.".
    * 
    * @return translated "<b>Session expired.</b><br/> User is not authenticated or session timeout expired. Press OK to redirect to the Login Page."
    */
    @DefaultMessage("<b>Session expired.</b><br/> User is not authenticated or session timeout expired. Press OK to redirect to the Login Page.")
    @Key("dlgMsgSessionExpired")
    String dlgMsgSessionExpired();

    /**
    * Translated "Warning".
    * 
    * @return translated "Warning"
    */
    @DefaultMessage("Warning")
    @Key("dlgWarning")
    String dlgWarning();

    /**
    * #AbstractAsyncCallback messages
    */

    @DefaultMessage("Your last operation failed.")
    @Key("yourLastOpFailed")
    String yourLastOpFailed();

    @DefaultMessage("<b> {0} </b><br/>")
    @Key("boldMessage")
    String boldMessage(String msg);

    @DefaultMessage("User does not have necessary privileges to perform this operation.")
    @Key("userDoesNotHave")
    String userDoesNotHave();

    @DefaultMessage("{0} ({1})<br/>{2}")
    @Key("errorMsgAlert")
    SafeHtml errorMsgAlert(String arg0, String arg1, String arg2);

    @DefaultMessage("{0} Server returns status code = {1}\n")
    @Key("serverReturns")
    String serverReturns(String msg, int code);

    @DefaultMessage("User is not authenticated or session timeout expired.<br/>Please re-login.")
    @Key("userIsNotAuthenticated")
    String userIsNotAuthenticated();

    @DefaultMessage("Sorry, but server has become unavailable. Please contact your server administrator.")
    @Key("serverUnavailable")
    String serverUnavailable();

    @DefaultMessage("Node was locked by another user. Please refresh the tree.")
    @Key("nodeLocked")
    String nodeLocked();

    @DefaultMessage("Node {0} does not exist, it may have been deleted by another user. <br/>Please reload parent node to make it up to date or verify that entire model has not been deleted.")
    @Key("nodeDoesNotExists")
    String nodeDoesNotExists(String node);

    /**
    * Translated "<div style='color:#666666'>&nbsp;Loading<small>...</small></div>".
    * 
    * @return translated "<div style='color:#666666'>&nbsp;Loading<small>...</small></div>"
    */
    @DefaultMessage("<div style='color:#666666'>&nbsp;Loading<small>...</small></div>")
    @Key("loadingHtmlMsg")
    String loadingHtmlMsg();

    @DefaultMessage("<div style=''color:#666666''>&nbsp;Contacting the Collaborate server<small>...</small></div>")
    @Key("contactingCollaborateServerMessage")
    String contactingCollaborateServerMessage();

    @DefaultMessage("N/A")
    @Key("notAvailable")
    String notAvailable();

    @DefaultMessage("of")
    @Key("of")
    String of();

    @DefaultMessage("next")
    @Key("next")
    String next();

    @DefaultMessage("previous")
    @Key("previous")
    String previous();

    @DefaultMessage("Please wait")
    @Key("pleaseWait")  
    String pleaseWait();

    @DefaultMessage("SM attributes are misconfigured, please see your administrator.")
    @Key("incorrectMapping")  
    String incorrectMapping();
}
