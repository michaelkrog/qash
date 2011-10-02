package dk.apaq.shopsystem.ui.shoppinnet.factory;

import com.vaadin.ui.CustomComponent;
import dk.apaq.shopsystem.service.OrganisationService;


public class AbstractFactory extends CustomComponent {
    
    public void Add(OrganisationService orgService, String id) {
        System.out.println("This was abstract add method...");
    }
    
}
