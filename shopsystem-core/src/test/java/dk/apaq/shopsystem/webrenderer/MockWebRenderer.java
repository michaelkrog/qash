package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author michael
 */
public class MockWebRenderer extends AbstractImageRenderer {

    private int calls;
    
    @Override
    public BufferedImage renderWebpageToImage(Device device, String url, boolean useCache) {
        try {
            calls++;
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/dk/apaq/shopsystem/webrenderer/testimage.jpg"));
            return image.getSubimage(0, 0, Math.min(image.getWidth(), device.getScreenWidth()), Math.min(image.getHeight(), device.getScreenHeight()));
        } catch (IOException ex) {
            return null;
        }
        
    }

    public int getCalls() {
        return calls;
    }
    
    
    
}
