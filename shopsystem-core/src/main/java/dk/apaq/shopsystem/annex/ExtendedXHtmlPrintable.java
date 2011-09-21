/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.simple.Graphics2DRenderer;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.XHTMLPrintable;

/**
 *
 * @author michael
 */
public class ExtendedXHtmlPrintable extends XHTMLPrintable {

    public ExtendedXHtmlPrintable(XHTMLPanel panel,float dpi) {
        super(panel);

        this.dpi=dpi;
    }

    public ExtendedXHtmlPrintable(XHTMLPanel panel) {
        this(panel,72);
    }
    private float dpi;

     
    @Override
    public int print(Graphics g, PageFormat pf, int page) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = new BufferedImage((int)pf.getWidth(),(int)pf.getHeight(),
                                           BufferedImage.TYPE_INT_RGB);
        Graphics2D layoutGraphics = (Graphics2D)image.getGraphics();

        if (g2r == null) {
                g2r = new Graphics2DRenderer();
                g2r.getSharedContext().setPrint(true);
                g2r.getSharedContext().setDotsPerPixel((int)(dpi/72));
                g2r.getSharedContext().setInteractive(false);
                g2r.getSharedContext().setDPI(dpi);
                g2r.getSharedContext().getTextRenderer().setSmoothingThreshold(0);
                g2r.getSharedContext().setUserAgentCallback(panel.getSharedContext().getUserAgentCallback());
                g2r.setDocument(panel.getDocument(), panel.getSharedContext().getUac().getBaseURL());
                g2r.getSharedContext().setReplacedElementFactory(panel.getSharedContext().getReplacedElementFactory());
                g2r.layout(layoutGraphics, null);
                g2r.getPanel().assignPagePrintPositions(g2);
            }
        return super.print(g, pf, page);
    }



}
