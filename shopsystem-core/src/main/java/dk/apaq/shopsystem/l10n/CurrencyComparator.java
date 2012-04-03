package dk.apaq.shopsystem.l10n;

import java.text.Collator;
import java.util.Comparator;
import java.util.Currency;

/**
 *
 * @author krog
 */
public class CurrencyComparator implements Comparator<Currency> {

    private final Comparator comparator;

    CurrencyComparator() {
        comparator = Collator.getInstance();
    }

    public int compare(Currency t, Currency t1) {
        return comparator.compare(t.getCurrencyCode(), t1.getCurrencyCode());
    }
}
