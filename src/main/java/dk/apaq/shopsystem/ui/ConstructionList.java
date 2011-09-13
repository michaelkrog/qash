/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.ui.common.Grid;

import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.Service;
import java.util.List;
import org.springframework.context.ApplicationContext;

 

/**
 *
 * @author Martin Christensen
 */
public class ConstructionList extends CustomComponent {
    
    //private ApplicationContext context;
    private Service service;
    
    public ConstructionList() {
        ApplicationContext context = VaadinSpringHelper.getSpringContextFromVaadinContext(getContext());
        service = context.getBean("service", Service.class);
        Organisation org = service.getOrganisationCrud().read("1");
           
        VerticalLayout Layout = new VerticalLayout();
        
        Grid grid = new Grid();
        grid.setData(org);
        //grid.setVisibleColumns(new Object[]{"delete", "number", "status", "dateChanged", "totalWithTax"});
        Layout.addComponent(grid);
        
        setCompositionRoot(Layout);
        
    }
    
}