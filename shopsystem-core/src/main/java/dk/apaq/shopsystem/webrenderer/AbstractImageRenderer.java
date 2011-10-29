package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;

/**
 *
 * @author michael
 */
public abstract class AbstractImageRenderer implements ImageRenderer {

    @Override
    public BufferedImage renderWebpageToImage(String url) {
        return this.renderWebpageToImage(new PcDevice(), url, true);
    }

    @Override
    public BufferedImage renderWebpageToImage(String url, boolean useCache) {
        return this.renderWebpageToImage(new PcDevice(), url, useCache);
    }

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url) {
        return this.renderWebpageToImage(device, url, true);
    }
    
    
    
    
}
