package dk.apaq.shopsystem.webrenderer;

import java.io.File;
import dk.apaq.shopsystem.webrenderer.WebRenderer.Device;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class FlyingSaucerWebRendererTest {
    
    public FlyingSaucerWebRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of renderWebpageToImage method, of class FlyingSaucerWebRenderer.
     */
    @Test
    public void testRenderWebpageToImage_WebRendererDevice_String() throws IOException {
        System.out.println("renderWebpageToImage");
        Device device = new Device() {

            @Override
            public int getScreenWidth() {
                return 595;
            }

            @Override
            public int getScreenHeight() {
                return 842;
            }
        };
        
        BufferedImage globe = ImageIO.read(getClass().getResourceAsStream("/rendertest_globe.jpg"));
        BufferedImage stonehenge = ImageIO.read(getClass().getResourceAsStream("/rendertest_stonehenge.jpg"));
        File file = new File("./src/test/resources/rendertest.html");
        String url = file.toURL().toString();
        FlyingSaucerWebRenderer instance = new FlyingSaucerWebRenderer();
        BufferedImage result = instance.renderWebpageToImage(device, url);
        
        //It should look like the globe.
        ImageComparer comparer = new ImageComparer(globe, result);
        comparer.compare();
        assertTrue(comparer.match());
        
        
        //... but nothing like the stonehenge.
        comparer = new ImageComparer(stonehenge, result);
        comparer.compare();
        assertFalse(comparer.match());
        
    }
}
