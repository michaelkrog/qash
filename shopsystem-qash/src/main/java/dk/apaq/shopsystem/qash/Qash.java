package dk.apaq.shopsystem.qash;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author krog
 */
public class Qash {

    private static final List<Locale> locales = Arrays.asList(new Locale[]{Locale.ENGLISH,
                /*Locale.FRENCH,
                Locale.CHINESE,
                Locale.JAPANESE,
                Locale.GERMAN,
                Locale.ITALIAN,
                new Locale("pt"),
                new Locale("es"),
                new Locale("is"),*/
                new Locale("da"),
                new Locale("sv"),
                new Locale("no")});

    public static final String QASH_I18N_BASE_NAME = "dk.apaq.shopsystem.qash.i18n.Messages";

    
    public static ResourceBundle getResourceBundle(Locale locale) {

        try {
            return ResourceBundle.getBundle(QASH_I18N_BASE_NAME, locale);
        } catch (MissingResourceException ex) {
            return ResourceBundle.getBundle(QASH_I18N_BASE_NAME, Locale.ENGLISH);
        }

    }
    public static List<Locale> getSupportedLocales() {
        return locales;
    }
}
