/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

/**
 *
 * @author michael
 */
public class SvgAnnexBundlePrinter implements AnnexPrinter{

    public void print(Printable printable, PageSize pageSize, OutputStream output) throws Exception {

        int width = pageSize.getPixelWidth();
        int height = pageSize.getPixelHeight();
        
        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width-5, height-5);
        pf.setPaper(paper);

        SVGGraphics2D g;

        boolean hasMorePages = true;
        int count = 0;

        ZipOutputStream zout = new ZipOutputStream(output);

        do {
            Document svgDocument = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().newDocument();
            SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(svgDocument);
            ctx.setPrecision(12);
            //  ctx.setEmbeddedFontsOn(true);

            //TODO Handle pagesize
            // Create an instance of the SVG Generator.
            g = new ExtendedSvgGraphics2D(ctx, false);

            g.setSVGCanvasSize(new Dimension(pageSize.getPixelWidth(72), pageSize.getPixelHeight(72)));

            if (printable.print(g, pf, count++) == Printable.PAGE_EXISTS) {
                ZipEntry entry = new ZipEntry(count + ".svg");
                zout.putNextEntry(entry);

                Writer writer = new OutputStreamWriter(zout, "utf-8");
                g.stream(writer, true);
                writer.flush();

                zout.flush();
                zout.closeEntry();
            } else {
                hasMorePages = false;
            }
        } while (hasMorePages);


        zout.flush();
        output.flush();
        zout.close();
    }

}
