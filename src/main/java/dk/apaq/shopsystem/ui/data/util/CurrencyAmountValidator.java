package dk.apaq.shopsystem.ui.data.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyAmountValidator extends AbstractNumberValidator {


    public CurrencyAmountValidator(Locale locale) {
        super(generateNumberFormat(locale));
    }

    private static NumberFormat generateNumberFormat(Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf;
    }

}
