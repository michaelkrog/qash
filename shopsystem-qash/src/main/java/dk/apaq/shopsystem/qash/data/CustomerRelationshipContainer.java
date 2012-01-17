package dk.apaq.shopsystem.qash.data;

import com.vaadin.data.util.NestedMethodProperty;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.CustomerRelationship;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.CrudItem;

/**
 *
 * @author krog
 */
public class CustomerRelationshipContainer extends CrudContainer<String, CustomerRelationship>  {

    private String[] extraPropertyIds = {"customer.companyName", "customer.contactName", "customer.companyRegistration", 
                                        "customer.telephone", "customer.email", "customer.street", 
                                        "customer.city", "customer.stateOrProvince", "customer.postalCode", 
                                        "customer.countryCode", "customer.bankAccount", "customer.websiteUrl"};
    
    public CustomerRelationshipContainer(Crud<String, CustomerRelationship> crud) {
        super(crud, CustomerRelationship.class);
        
        for(String id : extraPropertyIds) {
            model.put(id, String.class);
        }
    }

    @Override
    protected CrudItem buildItem(CrudContainer<String, CustomerRelationship> container, Crud<String, CustomerRelationship> crud, String id, CustomerRelationship bean) {
        CrudItem<String, CustomerRelationship> item = super.buildItem(container, crud, id, bean);
        CustomerRelationship relationship = item.getBean();
        
        for(String current : extraPropertyIds) {
            item.addItemProperty(current, new NestedMethodProperty(relationship, current));
        }
       
        return item;
    }
    

}