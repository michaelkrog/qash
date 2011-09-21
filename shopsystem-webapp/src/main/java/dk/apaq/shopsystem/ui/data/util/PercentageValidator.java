package dk.apaq.shopsystem.ui.data.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PercentageValidator extends AbstractNumberValidator {

    public PercentageValidator(Locale locale) {
        super(generateNumberFormat(locale));
    }

    private static NumberFormat generateNumberFormat(Locale locale) {
        NumberFormat nf = NumberFormat.getPercentInstance(locale);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        return nf;
    }

     @Override
    public boolean isValid(Object value) {
        return super.isValid(fixValue(value));
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        super.validate(fixValue(value));
    }


    private String fixValue(Object value) {
        String str = (String) value;
        if(!str.endsWith("%")) {
            str = str + "%";
        }
        return str;
    }


}
