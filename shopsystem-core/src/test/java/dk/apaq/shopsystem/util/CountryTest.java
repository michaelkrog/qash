package dk.apaq.shopsystem.util;

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
 * @author michael
 */
public class CountryTest {
    
   

    /**
     * Test of isWithinEu method, of class Country.
     */
    @Test
    public void testIsWithinEu() {
        System.out.println("isWithinEu");
        Country instance = Country.getCountry("DE");
        assertEquals(true, instance.isWithinEu());
        
        instance = Country.getCountry("NO");
        assertEquals(false, instance.isWithinEu());
        
    }

    
    /**
     * Test of getCountryByIpaddress method, of class Country.
     */
    @Test
    public void testGetCountryByIpaddress() {
        System.out.println("getCountryByIpaddress");
        String ipaddress = "213.140.85.233";
        Country result = Country.getCountryByIpaddress(ipaddress);
        assertEquals("DK", result.getCode());
        
        ipaddress = "79.125.105.236";
        result = Country.getCountryByIpaddress(ipaddress);
        assertEquals("IE", result.getCode());
        
        ipaddress = "qwesdasd";
        result = Country.getCountryByIpaddress(ipaddress);
        assertEquals("DK", result.getCode());
        
    }

    
}
