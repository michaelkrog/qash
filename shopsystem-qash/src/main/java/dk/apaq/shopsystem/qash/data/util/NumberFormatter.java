package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Property;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author michaelzachariassenkrog
 */
public class NumberFormatter  extends AbstractNumberFormatter {


    public NumberFormatter(Property property) {
        this(property, Locale.getDefault());
    }

    public NumberFormatter(Property property, Locale locale) {
        super(property);
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        setNumberFormat(nf);
    }
}

