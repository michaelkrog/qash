package dk.apaq.shopsystem.util;

import java.awt.image.BufferedImage;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.swing.Java2DRenderer;

/**
 *
 * @author krog
 */
public class WebRenderer {
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
    
    public static BufferedImage renderWebpageToImage(String url) {
        return WebRenderer.renderWebpageToImage(new PcDevice(), url);
    }
    
    public static BufferedImage renderWebpageToImage(Device device, String url) {
        Java2DRenderer java2DRenderer = new Java2DRenderer(url, device.getScreenWidth());
        return java2DRenderer.getImage();
    }
}
