package dk.apaq.shopsystem.webrenderer;

import java.io.OutputStream;

/**
 *
 * @author michael
 */
public interface PdfRenderer {

    
    /**
     * Renders a pdf of several webpages retrieved by the given urls.
     * @param os The outputstream the pdf should be written to.
     * @param os
     * @param url 
     */
    public void renderWebpageToPdf(OutputStream os, String... url);
}
