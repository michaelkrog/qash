/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui.view;

import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import dk.apaq.shopsystem.ui.VaadinSpringHelper;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.Service;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.shopsystem.ui.VaadinSpringHelper;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
/**
 *
 * @author Martin Christensen
 */
public class OverView extends SplitPanel  {
    
     public Table OverView() {
         
        //ApplicationContext context =  VaadinSpringHelper.getSpringContextFromVaadinContext(this.getContext());
        //Service service = context.getBean(Service.class);

        //OrganisationCrud crud = service.getOrganisationCrud();
        //CrudContainer container = new CrudContainer(crud, Organisation.class);

        Table table = new Table();
        //table.setContainerDataSource(container);
        table.setVisibleColumns(new Object[] {"name", "address", "telephone"});
        table.setSizeFull();
        
        return table;
     }
 }