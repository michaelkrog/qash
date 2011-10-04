package dk.apaq.shopsystem.ui.shoppinnet.factory;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.service.OrganisationService;


public abstract class AbstractFactory extends CustomComponent {
    
    protected OrganisationService orgService;
    protected Container container;
    protected VerticalLayout layout = new VerticalLayout();
    
    public abstract void setCrudContainer();
    public abstract void CreateEdit(String id);
    
    
    public AbstractFactory() {
        setCompositionRoot(this.layout);
    }
        
    
    public void setOrgService(OrganisationService orgService) {
        
        if (this.orgService == null) {
            this.orgService = orgService;
            setCrudContainer();
        }
    }
        
    
    public void AddItem(OrganisationService orgService, String id) {
        
        setOrgService(orgService);
        id = this.container.addItem().toString();   
        CreateEdit(id);
    }
      
      
    public void EditItem(OrganisationService orgService, String id) {
        
        setOrgService(orgService);
        CreateEdit(id);
    }
     
    
    public void DeleteItem(OrganisationService orgService, String id) {

        setOrgService(orgService);
        this.container.removeItem(id);
    }
    
    
  
    

    
     
}
