/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.util.WebRenderer.Device;
import java.awt.image.BufferedImage;
import java.io.File;
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
public class WebRendererTest {
    
    public WebRendererTest() {
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
     * Test of renderWebpageToImage method, of class WebRenderer.
     */
    @Test
    public void testRenderWebpageToImage_String() throws IOException {
        System.out.println("renderWebpageToImage");
        System.setProperty("java.net.useSystemProxies", "true");
    
        String url = "http://sean-pollock.com/templates/simplelife/";
        //String url = "file:///c:/tmp/apaq.htm";
        BufferedImage result = WebRenderer.renderWebpageToImage(url);
        ImageIO.write(result, "png", new File("web.png"));
        //assertEquals(expResult, result);
        
    }

}
