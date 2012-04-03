package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.entity.Tax;
import java.util.ArrayList;
import java.util.List;
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
public class TaxToolTest {
    
    public TaxToolTest() {
    }

   

    /**
     * Test of getAddableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetAddableTaxValue_double_Tax() {
        System.out.println("getAddableTaxValue");
        long value = 10000;
        Tax tax = new Tax("Moms", 25);
        long expResult = 2500;
        long result = TaxTool.getAddableTaxValue(value, tax);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetAddableTaxValue_double_List() {
        System.out.println("getAddableTaxValue");
        long value = 10000;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 10));
        long expResult = 2000;
        long result = TaxTool.getAddableTaxValue(value, taxList, false);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddableTaxValues method, of class TaxTool.
     */
    @Test
    public void testGetAddableTaxValues() {
        System.out.println("getAddableTaxValues");
        long value = 10000;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        long[] expResult = new long[]{1000, 2000};
        long[] result = TaxTool.getAddableTaxValues(value, taxList, false);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getWithdrawableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValue_double_Tax() {
        System.out.println("getWithdrawableTaxValue");
        long value = 10000;
        Tax tax = new Tax("moms", 25);
        long expResult = 2000;
        long result = TaxTool.getWithdrawableTaxValue(value, tax);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWithdrawableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValue_double_List() {
        System.out.println("getWithdrawableTaxValue");
        long value = 13000;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        long expResult = 3000;
        long result = TaxTool.getWithdrawableTaxValue(value, taxList, false);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWithdrawableTaxValues method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValues() {
        System.out.println("getWithdrawableTaxValues");
        long value = 13000;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        long[] expResult = new long[]{1000, 2000};
        long[] result = TaxTool.getWithdrawableTaxValues(value, taxList, false);
        assertArrayEquals(expResult, result);
    }
}
