package dk.apaq.shopsystem.webrenderer;

import java.io.File;
import dk.apaq.shopsystem.webrenderer.ImageRenderer.Device;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class FlyingSaucerRendererTest {
    
    /**
     * Test of renderWebpageToImage method, of class FlyingSaucerWebRenderer.
     */
    @Test
    public void testRenderWebpageToImage() throws IOException {
        
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
        FlyingSaucerRenderer instance = new FlyingSaucerRenderer();
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
    
    @Test
    public void testRenderWebpageWithSvgToImage() throws IOException {
        
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
        
        BufferedImage breakfeast = ImageIO.read(getClass().getResourceAsStream("/rendertest_breakfeast.png"));
        
        
        File file = new File("./src/test/resources/rendertestwithsvg.html");
        String url = file.toURL().toString();
        FlyingSaucerRenderer instance = new FlyingSaucerRenderer();
        BufferedImage result = instance.renderWebpageToImage(device, url);
        
        //It should look like the breakfeasts.
        ImageComparer comparer = new ImageComparer(result, breakfeast);
        comparer.compare();
        assertTrue(comparer.match());
        
        
    }
    
    @Test
    public void testRenderWebpagesToPdf() throws IOException {
        
        System.out.println("renderWebpagesToPdf");
        
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File file1 = new File("./src/test/resources/rendertest.html");
        File file2 = new File("./src/test/resources/rendertestwithsvg.html");
        String url1 = file1.toURL().toString();
        String url2 = file2.toURL().toString();
        FlyingSaucerRenderer instance = new FlyingSaucerRenderer();
        instance.renderWebpageToPdf(bos, url1, url2);
        
        assertNotSame(0, bos.size());
        
        
    }
    
    /*@Test
    public void testRenderWebpageWithSvgToPdf() throws IOException {
        
        System.out.println("renderWebpageWithSvgToPdf");
        
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File file = new File("./src/test/resources/rendertestwithsvg.html");
        String url = file.toURL().toString();
        FlyingSaucerRenderer instance = new FlyingSaucerRenderer();
        instance.renderWebpageToPdf(bos, url);
        
        assertNotSame(0, bos.size());
        
        
    }*/
        
}
