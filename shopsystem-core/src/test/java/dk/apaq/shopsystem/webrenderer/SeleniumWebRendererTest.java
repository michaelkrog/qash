package dk.apaq.shopsystem.webrenderer;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.awt.image.BufferedImage;
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

    
    //@Autowired
    //private WebRenderer webRenderer;

    /**
     * Test of renderWebpageToImage method, of class SeleniumWebRenderer.
     */
    @Test
    public void testRenderWebpageToImage_String() {
        System.out.println("renderWebpageToImage");
        String url = "http://www.google.com";
        
        //Disabled because we dont use this renderer right now.
        //BufferedImage result = webRenderer.renderWebpageToImage(url);
        //assertNotNull(result);
    }


}
