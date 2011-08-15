/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.data;

import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import dk.apaq.qash.share.model.Item;
import dk.apaq.qash.share.model.Tax;
import java.util.List;
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
public class ProductItemTest {

    private MockCrud crud = new MockCrud();
    private CrudContainer container = new CrudContainer(crud, Item.class);

    public ProductItemTest() {
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



    private class MockCrud implements Crud<String, Item> {

        public Item read(String id) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<String> listIds() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<String> listIds(Limit limit) {
            throw new UnsupportedOperationException("Not supported yet.");
        }



    }

    private class MockChangeHandler implements Property.ValueChangeListener {

        private ValueChangeEvent lastEvent = null;

        public void valueChange(ValueChangeEvent event) {
            this.lastEvent = event;
        }

        public void setLastEvent(ValueChangeEvent lastEvent) {
            this.lastEvent = lastEvent;
        }

        public ValueChangeEvent getLastEvent() {
            return lastEvent;
        }

    }

    @Test
    public void testGetType() {
        System.out.println("getItemProperty");

        Item item = new Item();

        ProductItem pi= new ProductItem(container, crud, item);
        MockChangeHandler handler = new MockChangeHandler();
        Property prop = pi.getItemProperty("priceWithTax");

        assertEquals(Double.class, prop.getType());
    }

    @Test
    public void testProductItem_PriceWithTaxDependencies() {
        System.out.println("getItemProperty");

        Item item = new Item();

        ProductItem pi= new ProductItem(container, crud, item);
        MockChangeHandler handler = new MockChangeHandler();
        Property prop = pi.getItemProperty("priceWithTax");
        ((Property.ValueChangeNotifier)prop).addListener(handler);

        Property dep1 = pi.getItemProperty("price");
        Property dep2 = pi.getItemProperty("tax");

        dep1.setValue(12.0);
        assertNotNull(handler.getLastEvent());

        handler.setLastEvent(null);

        dep2.setValue(new Tax("moms", 22.0));
        assertNotNull(handler.getLastEvent());
    }

    @Test
    public void testProductItem_PriceDependencies() {
        System.out.println("getItemProperty");

        Item item = new Item();

        ProductItem pi= new ProductItem(container, crud, item);
        MockChangeHandler handler = new MockChangeHandler();
        Property prop = pi.getItemProperty("price");
        ((Property.ValueChangeNotifier)prop).addListener(handler);

        Property dep1 = pi.getItemProperty("priceWithTax");

        dep1.setValue(12.0);
        assertNotNull(handler.getLastEvent());
    }

        @Test
    public void testProductItem_TaxValueDependencies() {

        Item item = new Item();

        ProductItem pi= new ProductItem(container, crud, item);
        MockChangeHandler handler = new MockChangeHandler();
        Property prop = pi.getItemProperty("taxValue");
        ((Property.ValueChangeNotifier)prop).addListener(handler);

        Property dep1 = pi.getItemProperty("price");
        Property dep2 = pi.getItemProperty("tax");
        Property dep3 = pi.getItemProperty("priceWithTax");

        dep1.setValue(12.0);
        assertNotNull(handler.getLastEvent());

        handler.setLastEvent(null);

        dep2.setValue(new Tax("moms", 22.0));
        assertNotNull(handler.getLastEvent());

        handler.setLastEvent(null);

        dep3.setValue(12.0);
        assertNotNull(handler.getLastEvent());
    }


}