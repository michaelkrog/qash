/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyUtil {

    public static List<Currency> getCountries() {
        return getCountries(Locale.getDefault());
    }

    public static List<Currency> getCountries(Locale locale) {
        Map<String, Currency> currencyMap = new HashMap<String, Currency>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale current : locales) {
            String iso = current.getISO3Country();
            String code = current.getCountry();
            String name = current.getDisplayCountry(locale);

            if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {

                Currency c = Currency.getInstance(current);
                if(c!=null && !currencyMap.containsKey(c.getCurrencyCode())) {
                    currencyMap.put(c.getCurrencyCode(), c);
                }
                
            }
        }

        List<Currency> currencies = new ArrayList(currencyMap.values());
        Collections.sort(currencies, new CurrencyComparator());
        return currencies;
    }
}
