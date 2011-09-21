package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.service.SystemService;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple test using the WicketTester
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
public class TestPage extends AbstractJUnit4SpringContextTests {

    private WicketTester tester;
    
    @Before
    public void setUp() {
        WebApplication app = applicationContext.getBean("wicketApplication", WebApplication.class);
        tester = new WicketTester(app);
    }

    @Test
    public void homepageRendersSuccessfully() {
        //start and render the test page
        //tester.startPage(WicketPage.class);

        //assert rendered page class
        //tester.assertRenderedPage(WicketPage.class);
    }
}
