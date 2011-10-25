package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;

/**
 *
 * @author michael
 */
public abstract class AbstractImageRenderer implements ImageRenderer {

    @Override
    public BufferedImage renderWebpageToImage(String url) {
        return this.renderWebpageToImage(new PcDevice(), url);
    }
    
}
