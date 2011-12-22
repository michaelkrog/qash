package dk.apaq.shopsystem.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Simple util class for I18N.
 * @author michael
 */
public class LocaleUtil {

    public static final String SYSTEM_I18N_BASE_NAME = "dk.apaq.shopsystem.i18n.Messages";

    public static ResourceBundle getResourceBundle(String basename, Locale locale) {

        try {
            return ResourceBundle.getBundle(basename, locale);
        } catch (MissingResourceException ex) {
            return ResourceBundle.getBundle(basename, Locale.ENGLISH);
        }

    }
    
    public static ResourceBundle getResourceBundle(Locale locale) {

        try {
            return ResourceBundle.getBundle(SYSTEM_I18N_BASE_NAME, locale);
        } catch (MissingResourceException ex) {
            return ResourceBundle.getBundle(SYSTEM_I18N_BASE_NAME, Locale.ENGLISH);
        }

    }
    
    /**
     * Retrieves the first locale in the system found with the given 2-letter countrycode.
     * Returns null if nons found.
     * @return Locale or null.
     */
    public static Locale getLocaleFromCountryCode(String countryCode) {
        for(Locale l : Locale.getAvailableLocales()) {
            if(l.getCountry().equalsIgnoreCase(countryCode)) {
                return l;
            }
        }
        return null;
    }
}
