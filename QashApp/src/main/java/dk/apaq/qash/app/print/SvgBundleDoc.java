/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.print;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.attribute.DocAttributeSet;

/**
 *
 * @author michaelzachariassenkrog
 */
public class SvgBundleDoc implements Doc{

    private final byte[] data;
    private final String mimetype = "application/zip";

    public SvgBundleDoc(byte[] data) {
        this.data = data;
    }

    public DocFlavor getDocFlavor() {
        return new DocFlavor(mimetype, new byte[0].getClass().getCanonicalName());
    }

    public Object getPrintData() throws IOException {
        return data;
    }

    public DocAttributeSet getAttributes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Reader getReaderForText() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputStream getStreamForBytes() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
