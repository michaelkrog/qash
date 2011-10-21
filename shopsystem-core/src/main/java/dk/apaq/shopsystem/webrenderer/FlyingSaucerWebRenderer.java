package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;
import org.xhtmlrenderer.swing.Java2DRenderer;

/**
 *
 * @author michael
 */
public class FlyingSaucerWebRenderer extends AbstractWebRenderer{

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url) {
        Java2DRenderer renderer = new Java2DRenderer(url, device.getScreenWidth());
        return renderer.getImage();
    }
    
}
