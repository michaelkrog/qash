package dk.apaq.shopsystem.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 *
 * @author krog
 */
public class Country {

    private String iso;
    private String code;
    private String name;

    Country(String iso, String code, String name) {
        this.iso = iso;
        this.code = code;
        this.name = name;
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

    public static Country getCountry(String countryCode, Locale locale) {
        if(countryCode.length()<2 && countryCode.length()>3) {
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

