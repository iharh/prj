package mygwt.foundation.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface CommonImages extends ClientBundle {
    CommonImages INSTANCE = GWT.create(CommonImages.class);

    @Source(value = "close.gif")	
    ImageResource closeIcon();
    
    @Source(value = "ajaxIndicator.gif")	
    ImageResource ajaxIndicator();
}
