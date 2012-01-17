package dk.apaq.shopsystem.qash.data;

import com.vaadin.data.Item;
import dk.apaq.crud.Crud;
import dk.apaq.crud.core.CollectionCrud;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.CrudItem;
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
public class CustomerRelationshipContainerTest {
    
    

    /**
     * Test of buildItem method, of class CustomerRelationshipContainer.
     */
    @Test
    public void test() {
        Organisation organisation = new Organisation();
        organisation.setCompanyName("Apaq");
        CustomerRelationship relationship = new CustomerRelationship();
        relationship.setCustomer(organisation);
        relationship.setId("qwerty");
        List<CustomerRelationship> customerRelationships = new ArrayList<CustomerRelationship>();
        customerRelationships.add(relationship);
        
        CollectionCrud crud = new CollectionCrud(customerRelationships, new CollectionCrud.IdResolver() {

            @Override
            public String getIdForBean(Object bean) {
                return ((CustomerRelationship)bean).getId();
            }
        });
        
        CrudContainer<String, CustomerRelationship> container = new CustomerRelationshipContainer(crud);
        
        assertEquals(1, container.size());
        assertEquals("qwerty", container.firstItemId());
        
        Item item = container.getItem("qwerty");
        assertNotNull(item);
        assertEquals("Apaq", item.getItemProperty("customer.companyName").getValue());
        
        item.getItemProperty("customer.companyName").setValue("Test");
        assertEquals(organisation.getCompanyName(), "Test");
        
    }
}
