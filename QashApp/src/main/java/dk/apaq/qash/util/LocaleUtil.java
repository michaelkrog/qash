package dk.apaq.qash.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocaleUtil {

    public static final String SITE_I18N_BASE_NAME = "dk.apaq.qash.site.i18n.Messages";
    public static final String APP_I18N_BASE_NAME = "dk.apaq.qash.app.i18n.Messages";
    
    public static ResourceBundle getResourceBundle(String basename, Locale locale) {

        try {
            return ResourceBundle.getBundle(basename, locale);
        } catch (MissingResourceException ex) {
            return ResourceBundle.getBundle(basename, Locale.ENGLISH);
        }

    }
}
