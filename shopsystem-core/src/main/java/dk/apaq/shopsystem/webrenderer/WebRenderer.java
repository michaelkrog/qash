package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;

/**
 *
 * @author krog
 */
public interface WebRenderer {
    
    public interface Device {
        int getScreenWidth();
        int getScreenHeight();
    }
    
    public static class PcDevice implements Device {

        
        @Override
        public int getScreenWidth() {
            return 1024;
        }

        @Override
        public int getScreenHeight() {
            return 768;
        }
        
    }
    
    /**
     * Renders an image of a webpage retrieved by the given url.
     * @param url The url to retrieve the webpage to render from.
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(String url);
    
    /**
     * Renders an image of a webpage retrieved by the given url.
     * @param url The url to retrieve the webpage to render from.
     * @param device Information about the device which the image
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(Device device, String url) ;
}
