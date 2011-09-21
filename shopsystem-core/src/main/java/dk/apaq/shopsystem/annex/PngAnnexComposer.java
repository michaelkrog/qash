/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author michael
 */
public class PngAnnexComposer implements AnnexComposer {

    public void compose(JComponent component, OutputStream output) throws Exception {
        Dimension dim = component.getSize();
        BufferedImage page = new BufferedImage(
                (int) dim.getWidth(), (int) dim.getHeight(), BufferedImage.TYPE_INT_ARGB);

        component.paintAll(page.createGraphics());
        ImageIO.write(page, "png", output);
        

        output.flush();

    }

}
