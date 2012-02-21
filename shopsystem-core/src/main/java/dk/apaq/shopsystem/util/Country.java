package dk.apaq.shopsystem.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 *
 * @author krog
 */
public class Country {

    private static final List<String> ISO_EU_LIST = Arrays.asList(new String[]{"BE", "BG", "CZ", "DK", "DE", "EE", "IE", "EL", 
                                                                                "ES", "FR", "IT", "CY", "LV", "LT", "LU", "HU", 
                                                                                "MT", "NL", "AT", "PT", "PL", "RO", "SI", "SK", 
                                                                                "FI", "SE", "UK"});
    private String iso;
    private String code;
    private String name;

    Country(String iso, String code, String name) {
        this.iso = iso;
        this.code = code;
        this.name = name;
    }

    public boolean isWithinEu() {
        return ISO_EU_LIST.contains(code);
    }
    
    public String getCode() {
        return code;
    }

    public String getIso() {
        return iso;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return iso + " - " + code + " - " + name.toUpperCase();
    }

    /**
     * Retrieves a country from the specified countryCode and translated using the given locale.
     */
    public static Country getCountry(String countryCode, Locale locale) {
        if(locale == null) {
            locale = Locale.getDefault();
        }
        
        if(countryCode == null || countryCode.length()<2 || countryCode.length()>3) {
            throw new IllegalArgumentException("Country code must be either an ISO 3166 2-letter code or an ISO 3166 3-letter code.");
        }

        if(countryCode.length() == 2) {
            for(Country c : getCountries(locale)) {
                if(countryCode.equals(c.getCode())) {
                    return c;
                }
            }
        } else {
            for(Country c : getCountries(locale)) {
                if(countryCode.equals(c.getIso())) {
                    return c;
                }
            }
        }
        return null;
    }
    
    public static List<Country> getCountries() {
        return getCountries(Locale.getDefault());
    }

    public static List<Country> getCountries(Locale locale) {
        List<Country> countries = new ArrayList<Country>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale current : locales) {
            String iso = getIsoCountry(current);
            String code = current.getCountry();
            String name = current.getDisplayCountry(locale);

            if (iso!=null && !"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                countries.add(new Country(iso, code, name));
            }
        }

        Collections.sort(countries, new CountryComparator());
        return countries;
    }
    
    private static String getIsoCountry(Locale locale) {
        try {
            return locale.getISO3Country();
        } catch(MissingResourceException ex) {
            return null;
        }
    }
}

