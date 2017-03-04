package mygwt.common.i18n;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

public class ResUtils {
    private static final Logger LOG = Logger.getLogger(ResUtils.class);
    
    // User's locale is populated in the UserLocaleSupportFilter from the the current HttpRequest and user's
    // settings. It is available wherever needed in server side code.
    private static final ThreadLocal<Locale> userLocale = new ThreadLocal<Locale>();
    
    // Handy method for getting localized resource bundle. Uses user's locale from the current request context.
    public static ResourceBundle getBundle(String bundle) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return getBundle(bundle, getUserLocale(), loader);
    }
    
    public static ResourceBundle getBundle(String bundle, String locale) {
        return getBundle(bundle, toLocale(locale));
    }
    
    public static ResourceBundle getBundle(String bundle, Locale locale) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return getBundle(bundle, locale, loader);
    }
    
    public static ResourceBundle getBundle(String bundle, Locale locale, ClassLoader loader) {
        return ResourceBundle.getBundle(bundle, locale == null ? Locale.ENGLISH : locale, loader);
    }
    
    // Ad hoc message formatting from the given bundle.
    // When formatting many messages an once use MsgFormatter instead.
    public static String getMessage(String bundleName, String key, Object... args) {
        String message = key;

        ResourceBundle bundle = getBundle(bundleName);
        if (bundle != null) {
            try {
                message = bundle.getString(key);
            } catch (MissingResourceException e) {
                LOG.warn(e);
                return key;
            } catch (ClassCastException e) {
                LOG.warn(e);
                return key;
            }
            if (args != null) {
                message = format(message, args);
            }
        }
        return message;
    }

    public static String format(final String message, final Object... args) {
        return new MessageFormat(message, getUserLocale()).format(args);
    }
    
    // Handy class for formatting msgs from the given res bundle.
    public static MsgFormatter getFormatter(ResourceBundle bundle) {
        return new MsgFormatter(bundle);
    }
    
    public static String getMsg(String bundleName, String key) {
    	return getBundle(bundleName).getString(key);
    }

    public static class MsgFormatter {
        private ResourceBundle bundle;
        private MessageFormat format;
        
        private MsgFormatter(ResourceBundle bundle) {
            this.bundle = bundle;
            this.format = new MessageFormat("");
            format.setLocale(getUserLocale());
        }
        
        /**
         * Returns formatted localized message.
         * 
         * @param key message key
         * @param args arguments for message place holders
         * @return formatted message
         */
        public String getMsg(String key, Object...args) {
            String message = bundle.getString(key);
            if (args != null) {
                format.applyPattern(message);
                message = format.format(args);
            }
            if (message != null) {
                return message;    
            } else {
                LOG.error("Message with key ?" + key + "? doesn't exists");
                return "?" + key  + "?";
            }
            
        }
    }

    public static Locale getUserLocale() {
        return userLocale.get() == null ? Locale.ROOT : userLocale.get();
    }
    
    public static void setUserLocale(Locale locale) {
        userLocale.set(locale);
    }

    // Convenient method for safe getting Locale from string. Apache's LocaleUtils doesn't handle empty string correctly.
    public static Locale toLocale(String localeStr) {
        return StringUtils.isBlank(localeStr) ? Locale.ROOT : LocaleUtils.toLocale(localeStr);
    }
}
