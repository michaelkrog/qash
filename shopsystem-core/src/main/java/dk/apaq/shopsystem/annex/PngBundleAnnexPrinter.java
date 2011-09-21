/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author michael
 */
public class PngBundleAnnexPrinter implements AnnexPrinter {

    public void print(Printable printable, PageSize pageSize, OutputStream output) throws Exception {
        BufferedImage page;
        Graphics g;
        int width = pageSize.getPixelWidth();
        int height = pageSize.getPixelHeight();

        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width-5, height-5);
        pf.setPaper(paper);

        boolean hasMorePages = true;
        int count = 0;

        ZipOutputStream zout = new ZipOutputStream(output);
        try {

            do {
                page = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                g = page.getGraphics();
                if (printable.print(g, pf, count++) == Printable.PAGE_EXISTS) {
                    ZipEntry entry = new ZipEntry(count + ".png");
                    zout.putNextEntry(entry);
                    ImageIO.write(page, "png", zout);
                    zout.flush();
                    zout.closeEntry();
                    page.flush();
                } else {
                    hasMorePages = false;
                }
            } while (hasMorePages);

        } catch (Exception ex) {
            throw ex;
        } finally {

            zout.flush();
            output.flush();
            zout.close();
        }


    }

}
