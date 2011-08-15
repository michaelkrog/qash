/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.util;

import java.util.List;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyUtilTest {

    public CurrencyUtilTest() {
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
     * Test of getCountries method, of class CurrencyUtil.
     */
    @Test
    public void testGetCountries() {
        System.out.println("getCountries");
        List result = CurrencyUtil.getCountries();
        assertFalse(result.isEmpty());
    }


}