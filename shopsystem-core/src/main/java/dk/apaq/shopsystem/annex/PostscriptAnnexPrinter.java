/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.print.Printable;
import java.io.OutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

/**
 *
 * @author michael
 */
public class PostscriptAnnexPrinter implements AnnexPrinter{

    public void print(Printable printable, PageSize pageSize, OutputStream output) throws Exception {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        StreamPrintServiceFactory[] factories =
                StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor,
                "application/postscript");

        StreamPrintService sps = null;

        if (factories.length == 0) {
            throw new Exception("No suitable factory.");
        }

        sps = factories[0].getPrintService(output);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        MediaSizeName mediaSizeName = MediaSize.findMedia(pageSize.getWidth(), pageSize.getHeight(), MediaSize.MM);
        aset.add(mediaSizeName);
        aset.add(new MediaPrintableArea(0, 0, pageSize.getWidth(), pageSize.getHeight(), Size2DSyntax.MM));
        Doc doc = new SimpleDoc(printable, flavor, null);

        DocPrintJob job = sps.createPrintJob();


        job.print(doc, aset);
    }

}
