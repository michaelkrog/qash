package dk.apaq.shopsystem.i18n;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple util class for I18N.
 * @author michael
 */
public class LocaleUtil {

    public static final String SYSTEM_I18N_BASE_NAME = "dk.apaq.shopsystem.i18n.Messages";
    private static final Logger LOG = LoggerFactory.getLogger(LocaleUtil.class);
    
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
        for (Locale l : Locale.getAvailableLocales()) {
            if (l.getCountry().equalsIgnoreCase(countryCode)) {
                return l;
            }
        }
        return null;
    }

   
}
