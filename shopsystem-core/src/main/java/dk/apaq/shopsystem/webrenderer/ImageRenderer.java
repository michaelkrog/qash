package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;

/**
 *
 * @author krog
 */
public interface ImageRenderer {
    
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
     * Renders an image of a webpage retrieved by the given url. Will use cache if available.
     * @param url The url to retrieve the webpage to render from.
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(String url);
    
    /**
     * Renders an image of a webpage retrieved by the given url.
     * @param url The url to retrieve the webpage to render from.
     * @param useCache Wether to use cache if available.
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(String url, boolean useCache);
    
    /**
     * Renders an image of a webpage retrieved by the given url. Will use cache if available.
     * @param device Information about the device which the image
     * @param url The url to retrieve the webpage to render from.
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(Device device, String url) ;
    
    /**
     * Renders an image of a webpage retrieved by the given url.
     * @param device Information about the device which the image
     * @param url The url to retrieve the webpage to render from.
     * @param useCache Wether to use cache if available.
     * @return The rendered image or null if unable to render. 
     */
    public BufferedImage renderWebpageToImage(Device device, String url, boolean useCache) ;
}
