package dk.apaq.shopsystem.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class SeleniumWebRenderer implements WebRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(SeleniumWebRenderer.class);
    
    private final ChromeDriverService service;

    public SeleniumWebRenderer(String chromeDriverPath) throws IOException {
        service = new ChromeDriverService.Builder()
        .usingChromeDriverExecutable(new File(chromeDriverPath))
        .usingAnyFreePort()
        .build();
        service.start();
    }
    
    private SeleniumWebRenderer() throws IOException{
        service = ChromeDriverService.createDefaultService();
        service.start();
    }
  
    @Override
    public BufferedImage renderWebpageToImage(String url) {
        return this.renderWebpageToImage(new PcDevice(), url);
    }

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url) {
        WebDriver driver = null;
        try {
            long start = System.currentTimeMillis();
      
            driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
            driver = new Augmenter().augment(driver); 
            driver.get(url);
            
            byte[] data = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            
            if(LOG.isDebugEnabled()) {
                LOG.debug("Rendered image in " + (System.currentTimeMillis() - start)+"ms.");
            }
            
            return image;
        } catch (IOException ex) {
            LOG.info("Unable to render image from webpage.", ex);
            return null;
        } finally {
            driver.close();
        }
    }
    
}
