package dk.apaq.shopsystem.webrenderer;

import dk.apaq.shopsystem.webrenderer.CachingWebRenderer;
import dk.apaq.shopsystem.webrenderer.WebRenderer.Device;
import dk.apaq.vfs.impl.ram.RamFilesystem;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class CachingWebRendererTest {
    
    public CachingWebRendererTest() {
    }

    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of renderWebpageToImage method, of class CachingWebRenderer.
     */
    @Test
    public void testRenderWebpageToImage_WebRendererDevice_String() throws FileNotFoundException, InterruptedException {
        System.out.println("renderWebpageToImage");
        
        RamFilesystem fs = new RamFilesystem();
        
        String url = "";
        MockWebRenderer mockWebRenderer = new MockWebRenderer();
        CachingWebRenderer instance = new CachingWebRenderer(mockWebRenderer, fs.getRoot(), 500);
        BufferedImage result = instance.renderWebpageToImage(url);
        
        assertEquals(1, fs.getRoot().getChildren().size());
        
        result = instance.renderWebpageToImage(url);
        
        Thread.sleep(600);
        
        result = instance.renderWebpageToImage(url);
        assertEquals(2, mockWebRenderer.getCalls());
        
        assertEquals(1, instance.getHits());
        assertEquals(1, instance.getExpireds());
        assertEquals(2, instance.getMisses());
        
    }
}
