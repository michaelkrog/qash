package dk.apaq.shopsystem.l10n;

import au.com.bytecode.opencsv.CSVReader;
import dk.apaq.shopsystem.i18n.LocaleUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for handling Country information.
 * 
 * Ip-to-Country data are retrieved from http://software77.net/geo-ip/
 * @author krog
 */
public class Country {

    private static class IPEntry {
        long from, to;
        String countrycode;
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(Country.class);
    private static List<IPEntry> ipEntries = null;
    private static final List<String> ISO_EU_LIST = Arrays.asList(new String[]{"BE", "BG", "CZ", "DK", "DE", "EE", "IE", "EL", 
                                                                                "ES", "FR", "IT", "CY", "LV", "LT", "LU", "HU", 
                                                                                "MT", "NL", "AT", "PT", "PL", "RO", "SI", "SK", 
                                                                                "FI", "SE", "UK"});
    
    static {
        loadIpEntries();
    }
     
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
    public static Country getCountry(String countryCode) {
        return getCountry(countryCode, null);
    }
    
    /**
     * Retrieves a country from the specified countryCode and translated using the given locale.
     */
    public static Country getCountry(String countryCode, Locale locale) {
        if(locale == null) {
            locale = Locale.getDefault();
        }
        
        if(countryCode == null || countryCode.length()<2 || countryCode.length()>3) {
            throw new IllegalArgumentException("Country code must be either an ISO 3166 2-letter code or an ISO 3166 3-letter code. [countryCode="+countryCode+"]");
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
    
        /**
     * This method will lookup the ip and check which country it comes from. If it fails to do so
     * if will default to an English locale. 
     * @param address The address to lookup.
     * @return The locale.
     */
    public static Country getCountryByIpaddress(String ipaddress) {

        long longIp = ipToLong(ipaddress);
        for (IPEntry entry : ipEntries) {
            if (entry.from <= longIp && entry.to >= longIp) {
                return getCountry(entry.countrycode);
            }
        }
        return getCountry("DK");
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
    


    private static void loadIpEntries() {
        try {
           
            ipEntries = new ArrayList<IPEntry>();
            InputStream stream = LocaleUtil.class.getResourceAsStream("/dk/apaq/shopsystem/i18n/IpToCountry.csv");
            InputStreamReader isreader = new InputStreamReader(stream);
            CSVReader reader = new CSVReader(isreader);
            List<String[]> list = (List<String[]>) reader.readAll();

            for (String[] array : list) {
                IPEntry entry = new IPEntry();
                entry.from = Long.parseLong(array[0]);
                entry.to = Long.parseLong(array[1]);
                entry.countrycode = array[4];
                ipEntries.add(entry);
            }
        } catch (IOException e) {
            LOG.error("Unable to load ip-to-country file.", e);
        }
    }

    private static Long ipToLong(String addr) {
        //TODO Support ip6
        try{ 
            String[] addrArray = addr.split("\\.");

            long num = 0;
            for (int i = 0; i < addrArray.length; i++) {
                int power = 3 - i;

                num += ((Long.parseLong(addrArray[i]) % 256 * Math.pow(256, power)));
            }

            return num;
        } catch(NumberFormatException ex) {
            return -1L;
        }
    }
}

