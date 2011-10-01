package dk.apaq.shopsystem.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
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
public class ScriptAnnotationTest {
    
    public ScriptAnnotationTest() {
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
     * Test of read method, of class ScriptAnnotation.
     */
    @Test
    public void testRead() throws Exception {
        String data = "\n/*\n * @Description(value=\"Her er en forklaring\")\n"+
                        "@Parameter(name=\"text\",type=\"String\") \n*/"+
                        "@Parameter(name=\"illegal\",type=\"String\")";
        System.out.println("read");
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        List<ScriptAnnotation.Annotation> result = ScriptAnnotation.read(inputStream);
        
        assertEquals(2, result.size());
        assertEquals("Description", result.get(0).getName());
        assertEquals("Parameter", result.get(1).getName());
        assertEquals("text", result.get(1).getMap().get("name"));
        
        
    }
}
