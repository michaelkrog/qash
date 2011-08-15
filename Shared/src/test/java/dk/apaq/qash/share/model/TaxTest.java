/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.share.model;

import dk.apaq.shopsystem.model.Tax;
import junit.framework.TestCase;

/**
 *
 * @author michael
 */
public class TaxTest extends TestCase {
    
    public TaxTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEquals() {
        Tax tax = new Tax();
        Tax tax1 = new Tax();

        assertTrue(tax.equals(tax1));

        tax.setName("test");

        assertFalse(tax.equals(tax1));

    }

}
