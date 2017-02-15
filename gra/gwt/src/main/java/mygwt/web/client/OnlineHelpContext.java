package mygwt.web.client;

public interface OnlineHelpContext {
    /**
     * @return URL(page parameter) for online video help
     * This method should be overridden if some wizard page should have link on video resource. 
     */
    String getOnlineVideoURL();
}
