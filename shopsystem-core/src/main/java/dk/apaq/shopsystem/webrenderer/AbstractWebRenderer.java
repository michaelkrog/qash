package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;

/**
 *
 * @author michael
 */
public abstract class AbstractWebRenderer implements WebRenderer {

    @Override
    public BufferedImage renderWebpageToImage(String url) {
        return this.renderWebpageToImage(new PcDevice(), url);
    }
    
}
