package dk.apaq.shopsystem.webrenderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
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
public class SeleniumWebRenderer extends AbstractImageRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(SeleniumWebRenderer.class);
    
    private final ChromeDriverService service;

    public SeleniumWebRenderer(String chromeDriverPath) throws IOException {
        service = new ChromeDriverService.Builder()
        .usingChromeDriverExecutable(new File(chromeDriverPath))
        .usingAnyFreePort()
        .build();
        init();
    }
    
    public SeleniumWebRenderer() throws IOException{
        service = ChromeDriverService.createDefaultService();
        init();
    }
    
    private void init() throws IOException {
        service.start();
        setupShutdownHook();
    }
    
    private void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                service.stop();
            }
            
        });
    }

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url, boolean useCache) {
        WebDriver driver = null;
        try {
            long start = System.currentTimeMillis();
      
            DesiredCapabilities caps = DesiredCapabilities.chrome();
            caps.setCapability("chrome.verbose", true);
            driver = new RemoteWebDriver(service.getUrl(), caps);
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
            if(driver!=null) {
                driver.close();
            }
        }
    }
    
}
