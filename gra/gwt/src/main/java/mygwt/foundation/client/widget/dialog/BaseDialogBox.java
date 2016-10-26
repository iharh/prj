package mygwt.foundation.client.widget.dialog;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ESCAPE;

import mygwt.foundation.client.util.ClientUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class BaseDialogBox extends DialogBox {
    /**
     * Show/hide events for applet Safari issues. IFrame shim doesn't work in Safari, so we need hide 
     * an applet when any popup is shown over it and show it back when popup gets hidden.
     * Applets (Word Cloud) will register for handling PopupShowNotificationEvent.
     */
    //TODO think about moving to impl and plug in by means of deferred binding
    private static SimpleEventBus eventBus = new SimpleEventBus();
    
    /**
     * Popup close event is fired on timer in order to delay flipping applet with image if any other popup dialog
     * was shown from a popup menu item.
     */
    private static Timer fireEventTimer;
    private static final int FIRE_TIMER_DELAY = 250;
    
    private HandlerRegistration closeHadlerReg;
    
    protected static final int ROW_HEIGHT = 20; 
    
    // TODO get rid of this glass panel since DialogBox being PopupPanel already has built-in glass feature
    // why doesn't it suit?
    //private GlassPanel background; 
    private boolean initialized;
    
    private boolean hideCloseButton;
    
    private Label closeBtn = new Label();

    public BaseDialogBox(String caption, int width, int height) {
        this(caption, width, height, true);
    }
    
    public BaseDialogBox(String caption, int width, int height, boolean modal) {
        this(caption, width, height, modal, modal);
    }

    public BaseDialogBox(String caption, int width, int height, boolean modal, boolean withGlass) {
        super(false, modal);
        
        setAnimationEnabled(false);
        setGlassEnabled(withGlass);
        setAutoHideOnHistoryEventsEnabled(true);
        
        setWidth(width + "px"); //$NON-NLS-1$
        setHeight(height + "px"); //$NON-NLS-1$

        sinkEvents(Event.ONCONTEXTMENU);

        setCaption(caption);
    }
    
    protected void setCaption(String caption) {
        setHTML(caption);
    }

    protected ClickHandler getCloseHandler() {
        return new ClickHandler() {
            public void onClick(ClickEvent sender) {
                BaseDialogBox.this.hide();
            }
        };
    }
    
    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONCONTEXTMENU) {
            DOM.eventPreventDefault(event);
        }
        super.onBrowserEvent(event);
    }
    
    @Override
    public void show() {
        if (initialized) {
        	//attaching GlassPanel before show() is called, not when accessing dialogBox
        	//to avoid beforehand panel's attachment - CMP-13428
            /*if (background != null) {
                RootPanel.get().add(background); //add(background, 0, 0); // Make dialog box real modal;    
            }*/

            // send event to hide applet on Safari
            firePopupShow(true);

            super.show();
        } else {
            initialized = true;
            
            center(); //1st attempt to open
            
            // Close button
            closeBtn.setStyleName("closeButton"); //$NON-NLS-1$
            closeBtn.addClickHandler(getCloseHandler());
            HorizontalPanel closeContainer = new HorizontalPanel();
            closeContainer.setWidth("100%"); //$NON-NLS-1$
            Widget caption = (Widget) getCaption();
            caption.removeFromParent();
            orphan(caption);
            closeContainer.add(caption);
            
            if (!hideCloseButton) {
                closeContainer.add(closeBtn);
            }
            
            closeContainer.setCellHorizontalAlignment(caption, HasHorizontalAlignment.ALIGN_LEFT);
            closeContainer.setCellHorizontalAlignment(closeBtn, HasHorizontalAlignment.ALIGN_RIGHT);
            closeContainer.setCellVerticalAlignment(closeBtn, HasVerticalAlignment.ALIGN_MIDDLE);
            closeContainer.setCellWidth(closeBtn, "15px"); //$NON-NLS-1$
            Element td = getCellElement(0, 1);
            DOM.appendChild(td, closeContainer.getElement());
            adopt(closeContainer);
            //DOM.setElementAttribute(closeBtn.getElement(), "align", "right");
            
            //DOM.getChild(td, 0).getStyle().setDisplay(Display.INLINE);
            //DOM.setStyleAttribute(closeBtn.getElement(), "display", "inline");
           /* td = getCellElement(1, 1).getParentElement().cast();
            DOM.setElementAttribute(td, "colspan", "2");*/
            if (closeHadlerReg == null) {
                closeHadlerReg = addCloseHandler(new BaseDialogBoxCloseHandler());
            }
            
            //As well, as IE7 often has issues while specifying header height,
            //here's hacky height hard-coding
            td = getCellElement(0, 1).getParentElement().cast();
            DOM.setElementAttribute(td, "height", ROW_HEIGHT + "px"); //$NON-NLS-1$ //$NON-NLS-2$
            td = getCellElement(0, 0).getParentElement().cast();
            DOM.setElementAttribute(td, "height", ROW_HEIGHT + "px"); //$NON-NLS-1$ //$NON-NLS-2$
            td = getCellElement(0, 2).getParentElement().cast();
            DOM.setElementAttribute(td, "height", ROW_HEIGHT + "px"); //$NON-NLS-1$ //$NON-NLS-2$
            onOpen();
        }
    }

    protected void onClose() {
    }
    
    protected void onOpen() {
    }
    
    protected void addCloseButtonHandler(ClickHandler handler) {
    	closeBtn.addClickHandler(handler);
    }

    public boolean onKeyDownPreview(char key, int modifiers) {
        if (key == KEY_ESCAPE) {
            BaseDialogBox.this.hide();
        }
        return true;
    }

    private class BaseDialogBoxCloseHandler implements CloseHandler<PopupPanel> {
        @Override
        public void onClose(CloseEvent<PopupPanel> event) {
            firePopupShow(false);
            ((BaseDialogBox) event.getTarget()).onClose();
        }
    }
    
    @Override
    public void setPopupPosition(int left, int top) {
        super.setPopupPosition(left, top);
        
        // Hackish approach to get to the iFrame shim behind this popup, make sure iframe is positioned
        // along with its popup
        if (ClientUtils.isIE() || ClientUtils.isChrome()) { // no need to do it in Safari or Mozilla
            com.google.gwt.dom.client.Element popup = getElement();
            JavaScriptObject _frame = popup.getPropertyJSO("__frame"); //$NON-NLS-1$
            if (_frame != null) {
                com.google.gwt.dom.client.Element iframe = _frame.cast();

                Style style = iframe.getStyle();
                
                style.setLeft(popup.getOffsetLeft(), Unit.PX); //left = popup.left;
                style.setTop(popup.getOffsetTop(), Unit.PX); //top = popup.top;
                style.setWidth(popup.getOffsetWidth(), Unit.PX); //width = popup.clientWidth; //offsetWidth;
                style.setHeight(popup.getOffsetHeight(), Unit.PX); //height = popup.clientHeight; //offsetHeight;
            }
        }
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        /*if (background != null) {
            background.setVisible(visible);
        }*/
    }
    
    /**
     * Register for notifications about show/hide popup dialog events. This is used by the applet wrapping widget,
     * namely WordCloudChart and only under Safari.
     * 
     * @param handler
     * @return
     */
    public static HandlerRegistration addPopupShowNotificationHanlder(PopupShowNotificationHanlder handler) {
        return eventBus.addHandler(PopupShowNotificationEvent.TYPE, handler);
    }

    public static void firePopupShow(boolean displayPopup) {
        if (ClientUtils.isSafari()) {
            initCloseTimer();
            fireEventTimer.cancel();
            if (displayPopup) {
                PopupShowNotificationEvent.fire(true, eventBus);
            } else {
                fireEventTimer.schedule(FIRE_TIMER_DELAY);
            }
        }
    }
    
    private static void initCloseTimer() {
        if (fireEventTimer == null) {
            fireEventTimer = new Timer() {
                @Override
                public void run() {
                    PopupShowNotificationEvent.fire(false, eventBus);        
                }
            };
        }
    }

    public boolean isHideCloseButton() {
        return hideCloseButton;
    }

    public void setHideCloseButton(boolean hideCloseButton) {
        this.hideCloseButton = hideCloseButton;
    }
}

