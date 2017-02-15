package mygwt.foundation.client.widget;

import mygwt.foundation.client.resources.CommonImages;

import com.google.gwt.user.client.ui.Image;

public final class AjaxLoaderImage extends Image {
    private static final String URL = "images/ajaxIndicator.gif";

    public AjaxLoaderImage() {
        super(CommonImages.INSTANCE.ajaxIndicator());
        this.setWidth("16");
        this.setHeight("16");
    }
}
