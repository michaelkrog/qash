package dk.apaq.shopsystem.util;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class SeleniumWebRendererTest {
    
    public SeleniumWebRendererTest() {
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
    
    @Autowired
    private WebRenderer webRenderer;

    /**
     * Test of renderWebpageToImage method, of class SeleniumWebRenderer.
     */
    @Test
    public void testRenderWebpageToImage_String() {
        System.out.println("renderWebpageToImage");
        String url = "http://www.google.com";
        BufferedImage result = webRenderer.renderWebpageToImage(url);
        assertNotNull(result);
    }


}
