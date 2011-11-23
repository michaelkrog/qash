package dk.apaq.shopsystem.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * A filter for expanding the canvas of an image to a bigger size.
 * @author michael
 */
public class ExpandFilter extends AbstractBufferedImageOp {

    private final int width;
    private final int height;

    public ExpandFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int difWidth = width - src.getWidth();
        int difHeight = height - src.getHeight();
        int horizontalMargin = difWidth == 0 ? 0 : (int) (double)difWidth / 2;
        int verticalMargin = difHeight == 0 ? 0 : (int) (double)difHeight / 2;


        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height), dstCM.isAlphaPremultiplied(), null);
        }

        Graphics2D g = dst.createGraphics();
        g.drawRenderedImage(src, AffineTransform.getTranslateInstance(horizontalMargin, verticalMargin));
        g.dispose();

        return dst;
    }
}
