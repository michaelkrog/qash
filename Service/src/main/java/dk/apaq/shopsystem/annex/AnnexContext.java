package dk.apaq.shopsystem.annex;

import dk.apaq.shopsystem.OutputType;
import java.io.OutputStream;
import java.util.Locale;

/**
 *
 * @author michael
 */
public class AnnexContext<INTYPE, OUTTYPE>  {
    private final INTYPE input;
    private final Page page;
    private final int dpi;
    private final Locale locale;
    private final OUTTYPE output;

    public AnnexContext(INTYPE input, OUTTYPE output, Page page, int dpi, Locale locale) {
        this.input = input;
        this.page = page;
        this.dpi = dpi;
        this.locale = locale;
        this.output = output;
    }

    public AnnexContext(AnnexContext<INTYPE,? extends Object> context, OUTTYPE out) {
        this.input = context.input;
        this.page = context.page;
        this.dpi = context.dpi;
        this.locale = context.locale;
        this.output = out;
    }



    public INTYPE getInput() {
        return input;
    }

    public OUTTYPE getOutput() {
        return output;
    }

    public int getDpi() {
        return dpi;
    }

    public Locale getLocale() {
        return locale;
    }

    public Page getPage() {
        return page;
    }

    

}
