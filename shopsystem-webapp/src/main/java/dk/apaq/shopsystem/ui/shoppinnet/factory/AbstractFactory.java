package dk.apaq.shopsystem.ui.shoppinnet.factory;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.service.OrganisationService;


public class AbstractFactory extends CustomComponent {
    
    protected OrganisationService orgService;
    protected Container container;
    protected VerticalLayout layout = new VerticalLayout();
    
    

    
     
}
