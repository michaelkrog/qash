package dk.apaq.shopsystem.qash.data.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class NumberValidator extends AbstractNumberValidator {

    public NumberValidator(Locale locale) {
        super(generateNumberFormat(locale));
    }



    private static NumberFormat generateNumberFormat(Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        return nf;
    }
}
