/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Dimension;
import java.awt.Font;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

/**
 *
 * @author michael
 */
public class SvgAnnexComposer implements AnnexComposer {

    public void compose(JComponent component, OutputStream output) throws Exception {
        Document svgDocument = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().newDocument();
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(svgDocument);
        ctx.setPrecision(12);

        Dimension dim = component.getSize();

        SVGGraphics2D g = new ExtendedSvgGraphics2D(ctx, false);
        g.setSVGCanvasSize(dim);

        component.paintAll(g);

        OutputStreamWriter writer = new OutputStreamWriter(output, "utf-8");
        g.stream(writer);

        writer.flush();

    }

}
