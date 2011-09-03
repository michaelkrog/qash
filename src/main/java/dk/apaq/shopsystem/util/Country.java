package dk.apaq.shopsystem.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

    public static List<Country> getCountries() {
        return getCountries(Locale.getDefault());
    }

    public static List<Country> getCountries(Locale locale) {
        List<Country> countries = new ArrayList<Country>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale current : locales) {
            String iso = current.getISO3Country();
            String code = current.getCountry();
            String name = current.getDisplayCountry(locale);

            if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                countries.add(new Country(iso, code, name));
            }
        }

        Collections.sort(countries, new CountryComparator());
        return countries;
    }
}

