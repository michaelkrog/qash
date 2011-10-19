package dk.apaq.shopsystem;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author michael
 */
public class SeleniumExample  {
    private static ChromeDriverService service;
    private static WebDriver driver;
  
    public static void main(String[] args) throws IOException {
        //System.setProperty("webdriver.chrome.driver", "/Users/michael/Downloads/chromedriver");
        service = new ChromeDriverService.Builder()
        .usingChromeDriverExecutable(new File("/Users/michael/Downloads/chromedriver"))
        .usingAnyFreePort()
        .build();
    service.start();
    
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        //ChromeDriver driver = new ChromeDriver();
        driver = new RemoteWebDriver(service.getUrl(),
        DesiredCapabilities.chrome());

        driver = new Augmenter().augment(driver); 
    
        driver.get("http://test.qash.dk");
    
        long start = System.currentTimeMillis();
    
        // And now use this to visit Google
        driver.get("http://test.qash.dk");
        
        byte[] data = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
        
        ImageIO.write(img, "png", new File("selenium.png"));

        //Close the browser
        driver.quit();
        
        long end = System.currentTimeMillis();
        
        System.out.print("Took screenshot in " +(end-start)+"ms.");
                
        service.stop();
    }
}