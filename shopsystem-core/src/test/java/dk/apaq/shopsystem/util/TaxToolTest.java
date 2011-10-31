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
        double value = 100.0;
        Tax tax = new Tax("Moms", 25);
        double expResult = 25.0;
        double result = TaxTool.getAddableTaxValue(value, tax);
        assertEquals(expResult, result, 0.5);
    }

    /**
     * Test of getAddableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetAddableTaxValue_double_List() {
        System.out.println("getAddableTaxValue");
        double value = 100.0;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 10));
        double expResult = 20.0;
        double result = TaxTool.getAddableTaxValue(value, taxList, false);
        assertEquals(expResult, result, 0.5);
    }

    /**
     * Test of getAddableTaxValues method, of class TaxTool.
     */
    @Test
    public void testGetAddableTaxValues() {
        System.out.println("getAddableTaxValues");
        double value = 100.0;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        double[] expResult = new double[]{10, 20};
        double[] result = TaxTool.getAddableTaxValues(value, taxList, false);
        assertArrayEquals(expResult, result, 0.5);
    }

    /**
     * Test of getWithdrawableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValue_double_Tax() {
        System.out.println("getWithdrawableTaxValue");
        double value = 100.0;
        Tax tax = new Tax("moms", 25);
        double expResult = 20.0;
        double result = TaxTool.getWithdrawableTaxValue(value, tax);
        assertEquals(expResult, result, 0.5);
    }

    /**
     * Test of getWithdrawableTaxValue method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValue_double_List() {
        System.out.println("getWithdrawableTaxValue");
        double value = 130.0;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        double expResult = 30.0;
        double result = TaxTool.getWithdrawableTaxValue(value, taxList, false);
        assertEquals(expResult, result, 0.5);
    }

    /**
     * Test of getWithdrawableTaxValues method, of class TaxTool.
     */
    @Test
    public void testGetWithdrawableTaxValues() {
        System.out.println("getWithdrawableTaxValues");
        double value = 130.0;
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(new Tax("1", 10));
        taxList.add(new Tax("2", 20));
        double[] expResult = new double[]{10, 20};
        double[] result = TaxTool.getWithdrawableTaxValues(value, taxList, false);
        assertArrayEquals(expResult, result, 0.5);
    }
}
